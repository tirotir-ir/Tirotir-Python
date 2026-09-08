package com.example.engine

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class ExecutionResult(
    val output: String,
    val isSuccess: Boolean,
    val executionTimeMs: Long,
    val needsInput: Boolean = false,
    val inputPrompt: String = ""
)

class PythonInterpreter {

  // Simple state environment
  private val variables = mutableMapOf<String, Any>()
  private val functions = mutableMapOf<String, DefinedFunction>()

  data class DefinedFunction(
      val params: List<String>,
      val lines: List<String>
  )

  suspend fun execute(
      code: String,
      inputCallback: (suspend (String) -> String)? = null
  ): ExecutionResult = withContext(Dispatchers.Default) {
    val startTime = System.currentTimeMillis()
    val outputBuilder = StringBuilder()
    variables.clear()
    functions.clear()

    // Add common pre-defined values
    variables["True"] = true
    variables["False"] = false

    val rawLines = code.lines()
    var lineIdx = 0

    try {
      while (lineIdx < rawLines.size) {
        val line = rawLines[lineIdx].trimEnd()
        val trimmed = line.trim()

        if (trimmed.isEmpty() || trimmed.startsWith("#")) {
          lineIdx++
          continue
        }

        // Handle multi-line triple quoted comments
        if (trimmed.startsWith("'''") || trimmed.startsWith("\"\"\"")) {
          val delimiter = if (trimmed.startsWith("'''")) "'''" else "\"\"\""
          if (trimmed.length > 3 && trimmed.endsWith(delimiter)) {
            lineIdx++
            continue
          }
          lineIdx++
          while (lineIdx < rawLines.size && !rawLines[lineIdx].contains(delimiter)) {
            lineIdx++
          }
          lineIdx++
          continue
        }

        // Function definition: def name(params):
        if (trimmed.startsWith("def ")) {
          val defHeader = trimmed.substring(4, trimmed.indexOf(":")).trim()
          val parenOpen = defHeader.indexOf("(")
          val parenClose = defHeader.lastIndexOf(")")
          if (parenOpen != -1 && parenClose != -1) {
            val funcName = defHeader.substring(0, parenOpen).trim()
            val paramsStr = defHeader.substring(parenOpen + 1, parenClose).trim()
            val params = if (paramsStr.isEmpty()) emptyList() else paramsStr.split(",").map { it.trim().split("=")[0].trim() }
            
            // Collect function body by indentation
            val bodyLines = mutableListOf<String>()
            lineIdx++
            while (lineIdx < rawLines.size) {
              val nextLine = rawLines[lineIdx]
              if (nextLine.isNotBlank() && !nextLine.startsWith(" ") && !nextLine.startsWith("\t")) {
                break
              }
              bodyLines.add(nextLine)
              lineIdx++
            }
            functions[funcName] = DefinedFunction(params, bodyLines)
            continue
          }
        }

        // While loop
        if (trimmed.startsWith("while ")) {
          val conditionStr = trimmed.substring(6, trimmed.lastIndexOf(":")).trim()
          val bodyLines = mutableListOf<String>()
          lineIdx++
          while (lineIdx < rawLines.size) {
            val nextLine = rawLines[lineIdx]
            if (nextLine.isNotBlank() && !nextLine.startsWith(" ") && !nextLine.startsWith("\t")) {
              break
            }
            bodyLines.add(nextLine)
            lineIdx++
          }

          var loopGuard = 0
          while (evalBoolean(conditionStr) && loopGuard < 1000) {
            loopGuard++
            val shouldBreak = executeBlock(bodyLines, outputBuilder, inputCallback)
            if (shouldBreak) break
          }
          if (loopGuard >= 1000) {
            outputBuilder.appendLine("[Warning: Infinite loop protection triggered (>1000 iterations)]")
          }
          continue
        }

        // For loop: for x in range(...) or for item in list:
        if (trimmed.startsWith("for ") && trimmed.contains(" in ") && trimmed.endsWith(":")) {
          val header = trimmed.substring(4, trimmed.length - 1).trim()
          val parts = header.split(" in ")
          val varName = parts[0].trim()
          val iterableExpr = parts[1].trim()

          val bodyLines = mutableListOf<String>()
          lineIdx++
          while (lineIdx < rawLines.size) {
            val nextLine = rawLines[lineIdx]
            if (nextLine.isNotBlank() && !nextLine.startsWith(" ") && !nextLine.startsWith("\t")) {
              break
            }
            bodyLines.add(nextLine)
            lineIdx++
          }

          val items = evalIterable(iterableExpr)
          for (item in items) {
            variables[varName] = item
            val shouldBreak = executeBlock(bodyLines, outputBuilder, inputCallback)
            if (shouldBreak) break
          }
          continue
        }

        // If statement: if cond:
        if (trimmed.startsWith("if ") && trimmed.endsWith(":")) {
          val condStr = trimmed.substring(3, trimmed.length - 1).trim()
          val ifBody = mutableListOf<String>()
          lineIdx++
          while (lineIdx < rawLines.size) {
            val nextLine = rawLines[lineIdx]
            if (nextLine.isNotBlank() && !nextLine.startsWith(" ") && !nextLine.startsWith("\t")) {
              break
            }
            ifBody.add(nextLine)
            lineIdx++
          }

          var conditionMet = evalBoolean(condStr)
          if (conditionMet) {
            executeBlock(ifBody, outputBuilder, inputCallback)
          }

          // Check for elif / else
          while (lineIdx < rawLines.size) {
            val nextTrimmed = rawLines[lineIdx].trim()
            if (nextTrimmed.startsWith("elif ") && nextTrimmed.endsWith(":")) {
              val elifCond = nextTrimmed.substring(5, nextTrimmed.length - 1).trim()
              val elifBody = mutableListOf<String>()
              lineIdx++
              while (lineIdx < rawLines.size) {
                val nl = rawLines[lineIdx]
                if (nl.isNotBlank() && !nl.startsWith(" ") && !nl.startsWith("\t")) break
                elifBody.add(nl)
                lineIdx++
              }
              if (!conditionMet && evalBoolean(elifCond)) {
                executeBlock(elifBody, outputBuilder, inputCallback)
                conditionMet = true
              }
            } else if (nextTrimmed.startsWith("else:")) {
              val elseBody = mutableListOf<String>()
              lineIdx++
              while (lineIdx < rawLines.size) {
                val nl = rawLines[lineIdx]
                if (nl.isNotBlank() && !nl.startsWith(" ") && !nl.startsWith("\t")) break
                elseBody.add(nl)
                lineIdx++
              }
              if (!conditionMet) {
                executeBlock(elseBody, outputBuilder, inputCallback)
                conditionMet = true
              }
              break
            } else {
              break
            }
          }
          continue
        }

        // Single statement execution
        executeStatement(trimmed, outputBuilder, inputCallback)
        lineIdx++
      }

      val duration = System.currentTimeMillis() - startTime
      val finalOutput = if (outputBuilder.isEmpty()) "Program finished with exit code 0 (No console output)." else outputBuilder.toString()
      ExecutionResult(finalOutput, isSuccess = true, executionTimeMs = duration)
    } catch (e: Exception) {
      val duration = System.currentTimeMillis() - startTime
      val err = "Traceback (most recent call last):\n  Error: ${e.message ?: "Runtime Exception"}\n"
      ExecutionResult(outputBuilder.toString() + "\n" + err, isSuccess = false, executionTimeMs = duration)
    }
  }

  private suspend fun executeBlock(
      lines: List<String>,
      output: StringBuilder,
      inputCallback: (suspend (String) -> String)?
  ): Boolean {
    var i = 0
    while (i < lines.size) {
      val line = lines[i].trim()
      if (line.isEmpty() || line.startsWith("#")) {
        i++
        continue
      }
      if (line == "break") return true
      if (line == "continue") {
        i++
        continue
      }
      executeStatement(line, output, inputCallback)
      i++
    }
    return false
  }

  private suspend fun executeStatement(
      stmt: String,
      output: StringBuilder,
      inputCallback: (suspend (String) -> String)?
  ) {
    var line = stmt.trim()

    // Import statement (dummy acknowledgment for standard libraries)
    if (line.startsWith("import ") || line.startsWith("from ")) {
      return
    }

    // print(...) statement
    if (line.startsWith("print(") && line.endsWith(")")) {
      val inside = line.substring(6, line.length - 1).trim()
      val rendered = evaluatePrintArguments(inside)
      output.appendLine(rendered)
      return
    }

    // input() interactive assignment: var = input("prompt")
    if (line.contains("input(")) {
      val eqIdx = line.indexOf("=")
      if (eqIdx != -1) {
        val varName = line.substring(0, eqIdx).trim()
        val rightSide = line.substring(eqIdx + 1).trim()
        val prompt = extractInputPrompt(rightSide)
        val enteredValue = inputCallback?.invoke(prompt) ?: "tirOtir"

        // Check if wrapped in int() or float()
        if (rightSide.startsWith("int(")) {
          variables[varName] = enteredValue.toIntOrNull() ?: 0
        } else if (rightSide.startsWith("float(")) {
          variables[varName] = enteredValue.toDoubleOrNull() ?: 0.0
        } else {
          variables[varName] = enteredValue
        }
        output.appendLine(">>> $prompt$enteredValue")
        return
      }
    }

    // List method: list.append(x)
    if (line.contains(".append(") && line.endsWith(")")) {
      val dotIdx = line.indexOf(".append(")
      val listName = line.substring(0, dotIdx).trim()
      val argStr = line.substring(dotIdx + 8, line.length - 1).trim()
      val value = evalExpression(argStr)
      val listObj = variables[listName]
      if (listObj is MutableList<*>) {
        @Suppress("UNCHECKED_CAST")
        (listObj as MutableList<Any>).add(value)
      }
      return
    }

    // List method: list.remove(x)
    if (line.contains(".remove(") && line.endsWith(")")) {
      val dotIdx = line.indexOf(".remove(")
      val listName = line.substring(0, dotIdx).trim()
      val argStr = line.substring(dotIdx + 8, line.length - 1).trim()
      val value = evalExpression(argStr)
      val listObj = variables[listName]
      if (listObj is MutableList<*>) {
        (listObj as MutableList<*>).remove(value)
      }
      return
    }

    // Augmented assignment: var += expr, -=, *=, /=
    val augOps = listOf("+=", "-=", "*=", "/=")
    for (op in augOps) {
      if (line.contains(op)) {
        val parts = line.split(op)
        val varName = parts[0].trim()
        val delta = evalExpression(parts[1].trim())
        val current = variables[varName] ?: 0
        variables[varName] = applyMath(current, op.substring(0, 1), delta)
        return
      }
    }

    // Standard assignment: var = expr
    if (line.contains("=") && !line.contains("==") && !line.contains("<=") && !line.contains(">=") && !line.contains("!=")) {
      val eqIdx = line.indexOf("=")
      val leftSide = line.substring(0, eqIdx).trim()
      val rightSide = line.substring(eqIdx + 1).trim()

      // Dictionary item assignment: d["key"] = val
      if (leftSide.contains("[") && leftSide.endsWith("]")) {
        val bracketOpen = leftSide.indexOf("[")
        val dictName = leftSide.substring(0, bracketOpen).trim()
        val keyExpr = leftSide.substring(bracketOpen + 1, leftSide.length - 1).trim()
        val key = evalExpression(keyExpr).toString().replace("\"", "").replace("'", "")
        val value = evalExpression(rightSide)

        val dictObj = variables[dictName]
        if (dictObj is MutableMap<*, *>) {
          @Suppress("UNCHECKED_CAST")
          (dictObj as MutableMap<String, Any>)[key] = value
        } else {
          val newDict = mutableMapOf<String, Any>()
          newDict[key] = value
          variables[dictName] = newDict
        }
        return
      }

      val value = evalExpression(rightSide)
      variables[leftSide] = value
      return
    }

    // Standalone function call
    if (line.contains("(") && line.endsWith(")")) {
      evalExpression(line)
    }
  }

  private fun extractInputPrompt(expr: String): String {
    val inputIdx = expr.indexOf("input(")
    if (inputIdx == -1) return "Enter value: "
    val start = inputIdx + 6
    val end = expr.indexOf(")", start)
    if (end != -1) {
      val p = expr.substring(start, end).trim()
      return p.removeSurrounding("\"").removeSurrounding("'")
    }
    return "Enter value: "
  }

  private fun evaluatePrintArguments(argsStr: String): String {
    if (argsStr.isEmpty()) return ""

    // Handle f-string: f"Text {var}" or f'Text {var}'
    if (argsStr.startsWith("f\"") || argsStr.startsWith("f'")) {
      val raw = argsStr.substring(2, argsStr.length - 1)
      return renderFString(raw)
    }

    // Split args respecting quotes and brackets
    val tokens = splitRespectingQuotes(argsStr)
    return tokens.joinToString(" ") { token ->
      evalExpression(token).toString()
    }
  }

  private fun renderFString(template: String): String {
    val regex = "\\{([^}]+)\\}".toRegex()
    return regex.replace(template) { matchResult ->
      val expr = matchResult.groupValues[1].trim()
      // format specifier support, e.g. {avg:.2f}
      val formatParts = expr.split(":")
      val coreExpr = formatParts[0].trim()
      val evaluated = evalExpression(coreExpr)

      if (formatParts.size > 1 && evaluated is Number) {
        val spec = formatParts[1].trim()
        if (spec.endsWith("f")) {
          val decCount = spec.removeSuffix("f").removePrefix(".").toIntOrNull() ?: 2
          return@replace String.format("%.${decCount}f", evaluated.toDouble())
        }
      }
      evaluated.toString()
    }
  }

  fun evalExpression(expr: String): Any {
    val trimmed = expr.trim()

    // Boolean literals
    if (trimmed == "True") return true
    if (trimmed == "False") return false
    if (trimmed == "None") return "None"

    // String literals
    if ((trimmed.startsWith("\"") && trimmed.endsWith("\"")) ||
        (trimmed.startsWith("'") && trimmed.endsWith("'"))) {
      return trimmed.substring(1, trimmed.length - 1)
          .replace("\\n", "\n")
          .replace("\\t", "\t")
    }

    // Numeric literals
    trimmed.toIntOrNull()?.let { return it }
    trimmed.toDoubleOrNull()?.let { return it }

    // List literal: [1, 2, 3]
    if (trimmed.startsWith("[") && trimmed.endsWith("]")) {
      val inside = trimmed.substring(1, trimmed.length - 1).trim()
      if (inside.isEmpty()) return mutableListOf<Any>()
      val items = splitRespectingQuotes(inside).map { evalExpression(it) }
      return items.toMutableList()
    }

    // Dict literal: {"k": v}
    if (trimmed.startsWith("{") && trimmed.endsWith("}")) {
      val inside = trimmed.substring(1, trimmed.length - 1).trim()
      val map = mutableMapOf<String, Any>()
      if (inside.isNotEmpty()) {
        val pairs = splitRespectingQuotes(inside)
        for (pair in pairs) {
          val colonIdx = pair.indexOf(":")
          if (colonIdx != -1) {
            val k = evalExpression(pair.substring(0, colonIdx).trim()).toString()
            val v = evalExpression(pair.substring(colonIdx + 1).trim())
            map[k] = v
          }
        }
      }
      return map
    }

    // Function calls: len(x), sum(x), min(x), max(x), type(x)
    if (trimmed.startsWith("len(") && trimmed.endsWith(")")) {
      val arg = evalExpression(trimmed.substring(4, trimmed.length - 1))
      return when (arg) {
        is Collection<*> -> arg.size
        is String -> arg.length
        is Map<*, *> -> arg.size
        else -> 0
      }
    }

    if (trimmed.startsWith("sum(") && trimmed.endsWith(")")) {
      val arg = evalExpression(trimmed.substring(4, trimmed.length - 1))
      if (arg is Collection<*>) {
        return arg.filterIsInstance<Number>().sumOf { it.toDouble() }
      }
      return 0
    }

    // User-defined functions
    val parenIdx = trimmed.indexOf("(")
    if (parenIdx != -1 && trimmed.endsWith(")")) {
      val funcName = trimmed.substring(0, parenIdx).trim()
      if (functions.containsKey(funcName)) {
        val func = functions[funcName]!!
        val argsStr = trimmed.substring(parenIdx + 1, trimmed.length - 1).trim()
        val argValues = if (argsStr.isEmpty()) emptyList() else splitRespectingQuotes(argsStr).map { evalExpression(it) }
        
        // Bind args
        val savedVars = HashMap(variables)
        func.params.forEachIndexed { i, p ->
          if (i < argValues.size) {
            variables[p] = argValues[i]
          }
        }

        var result: Any = "None"
        for (fLine in func.lines) {
          val flTrimmed = fLine.trim()
          if (flTrimmed.startsWith("return ")) {
            val returnExpr = flTrimmed.substring(7).trim()
            result = evalExpression(returnExpr)
            break
          }
        }
        variables.clear()
        variables.putAll(savedVars)
        return result
      }
    }

    // String methods: str.upper(), str.lower()
    if (trimmed.endsWith(".upper()")) {
      val target = trimmed.removeSuffix(".upper()").trim()
      return evalExpression(target).toString().uppercase()
    }
    if (trimmed.endsWith(".lower()")) {
      val target = trimmed.removeSuffix(".lower()").trim()
      return evalExpression(target).toString().lowercase()
    }

    // Dictionary or List lookup: obj[key]
    if (trimmed.contains("[") && trimmed.endsWith("]")) {
      val bracket = trimmed.indexOf("[")
      val objName = trimmed.substring(0, bracket).trim()
      val keyExpr = trimmed.substring(bracket + 1, trimmed.length - 1).trim()
      val obj = variables[objName]
      if (obj is List<*>) {
        val idx = evalExpression(keyExpr).toString().toIntOrNull() ?: 0
        val resolvedIdx = if (idx < 0) obj.size + idx else idx
        if (resolvedIdx in obj.indices) return obj[resolvedIdx] ?: "None"
      } else if (obj is Map<*, *>) {
        val key = evalExpression(keyExpr).toString()
        return obj[key] ?: "None"
      }
    }

    // Arithmetic expressions (+, -, *, /, %, **)
    if (trimmed.contains("+") || trimmed.contains("-") || trimmed.contains("*") || trimmed.contains("/") || trimmed.contains("%")) {
      return parseSimpleMath(trimmed)
    }

    // Variable lookup
    if (variables.containsKey(trimmed)) {
      return variables[trimmed]!!
    }

    return trimmed
  }

  private fun parseSimpleMath(expr: String): Any {
    // Check for power operator **
    if (expr.contains("**")) {
      val parts = expr.split("**")
      val base = evalExpression(parts[0].trim()).toString().toDoubleOrNull() ?: 0.0
      val exp = evalExpression(parts[1].trim()).toString().toDoubleOrNull() ?: 0.0
      val res = Math.pow(base, exp)
      return if (res % 1.0 == 0.0) res.toLong() else res
    }

    // Addition
    if (expr.contains("+") && !expr.startsWith("+")) {
      val parts = expr.split("+", limit = 2)
      val left = evalExpression(parts[0].trim())
      val right = evalExpression(parts[1].trim())
      if (left is String || right is String) {
        return left.toString() + right.toString()
      }
      return applyMath(left, "+", right)
    }

    // Subtraction
    if (expr.contains("-") && !expr.startsWith("-")) {
      val parts = expr.split("-", limit = 2)
      val left = evalExpression(parts[0].trim())
      val right = evalExpression(parts[1].trim())
      return applyMath(left, "-", right)
    }

    // Multiplication
    if (expr.contains("*")) {
      val parts = expr.split("*", limit = 2)
      val left = evalExpression(parts[0].trim())
      val right = evalExpression(parts[1].trim())
      return applyMath(left, "*", right)
    }

    // Division
    if (expr.contains("/")) {
      val parts = expr.split("/", limit = 2)
      val left = evalExpression(parts[0].trim()).toString().toDoubleOrNull() ?: 0.0
      val right = evalExpression(parts[1].trim()).toString().toDoubleOrNull() ?: 1.0
      return if (right == 0.0) "ZeroDivisionError" else (left / right)
    }

    return expr
  }

  private fun applyMath(a: Any, op: String, b: Any): Any {
    val n1 = a.toString().toDoubleOrNull() ?: 0.0
    val n2 = b.toString().toDoubleOrNull() ?: 0.0
    val res = when (op) {
      "+" -> n1 + n2
      "-" -> n1 - n2
      "*" -> n1 * n2
      "/" -> if (n2 != 0.0) n1 / n2 else 0.0
      "%" -> n1 % n2
      else -> n1 + n2
    }
    return if (res % 1.0 == 0.0 && a !is Double && b !is Double) res.toLong() else res
  }

  private fun evalBoolean(cond: String): Boolean {
    val trimmed = cond.trim()
    if (trimmed == "True") return true
    if (trimmed == "False") return false

    // Logical OR
    if (trimmed.contains(" or ")) {
      val parts = trimmed.split(" or ")
      return parts.any { evalBoolean(it) }
    }
    // Logical AND
    if (trimmed.contains(" and ")) {
      val parts = trimmed.split(" and ")
      return parts.all { evalBoolean(it) }
    }

    // Comparisons
    val ops = listOf("==", "!=", "<=", ">=", "<", ">")
    for (op in ops) {
      if (trimmed.contains(op)) {
        val parts = trimmed.split(op)
        val left = evalExpression(parts[0].trim())
        val right = evalExpression(parts[1].trim())
        return compareValues(left, op, right)
      }
    }

    // Check variable truthiness
    val evaluated = evalExpression(trimmed)
    return when (evaluated) {
      is Boolean -> evaluated
      is Number -> evaluated.toDouble() != 0.0
      is String -> evaluated.isNotEmpty()
      is Collection<*> -> evaluated.isNotEmpty()
      else -> false
    }
  }

  private fun compareValues(left: Any, op: String, right: Any): Boolean {
    val n1 = left.toString().toDoubleOrNull()
    val n2 = right.toString().toDoubleOrNull()
    if (n1 != null && n2 != null) {
      return when (op) {
        "==" -> n1 == n2
        "!=" -> n1 != n2
        "<" -> n1 < n2
        "<=" -> n1 <= n2
        ">" -> n1 > n2
        ">=" -> n1 >= n2
        else -> false
      }
    }
    val s1 = left.toString()
    val s2 = right.toString()
    return when (op) {
      "==" -> s1 == s2
      "!=" -> s1 != s2
      else -> false
    }
  }

  private fun evalIterable(expr: String): List<Any> {
    val trimmed = expr.trim()
    // range(start, stop, step)
    if (trimmed.startsWith("range(") && trimmed.endsWith(")")) {
      val args = trimmed.substring(6, trimmed.length - 1).split(",").map { evalExpression(it).toString().toIntOrNull() ?: 0 }
      return when (args.size) {
        1 -> (0 until args[0]).map { it }
        2 -> (args[0] until args[1]).map { it }
        3 -> {
          val list = mutableListOf<Int>()
          var curr = args[0]
          val stop = args[1]
          val step = args[2]
          if (step > 0) {
            while (curr < stop) {
              list.add(curr)
              curr += step
            }
          } else if (step < 0) {
            while (curr > stop) {
              list.add(curr)
              curr += step
            }
          }
          list
        }
        else -> emptyList()
      }
    }

    val evaluated = evalExpression(trimmed)
    if (evaluated is List<*>) {
      return evaluated.filterNotNull()
    }
    return emptyList()
  }

  private fun splitRespectingQuotes(input: String): List<String> {
    val result = mutableListOf<String>()
    var current = StringBuilder()
    var inQuotes = false
    var quoteChar = ' '
    var depth = 0

    for (c in input) {
      if ((c == '"' || c == '\'') && (depth == 0 || inQuotes)) {
        if (!inQuotes) {
          inQuotes = true
          quoteChar = c
        } else if (c == quoteChar) {
          inQuotes = false
        }
        current.append(c)
      } else if (c == '(' || c == '[' || c == '{') {
        depth++
        current.append(c)
      } else if (c == ')' || c == ']' || c == '}') {
        depth--
        current.append(c)
      } else if (c == ',' && !inQuotes && depth == 0) {
        result.add(current.toString().trim())
        current = StringBuilder()
      } else {
        current.append(c)
      }
    }
    if (current.isNotEmpty()) {
      result.add(current.toString().trim())
    }
    return result
  }
}
