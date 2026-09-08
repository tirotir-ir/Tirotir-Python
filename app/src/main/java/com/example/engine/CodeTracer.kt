package com.example.engine

data class VariableSnapshot(
    val name: String,
    val type: String,
    val value: String,
    val isChanged: Boolean = false
)

data class TraceStep(
    val stepIndex: Int,
    val lineNumber: Int,
    val lineCode: String,
    val callStack: List<String>,
    val scope: String,
    val variables: List<VariableSnapshot>,
    val stdout: String,
    val explanation: String
)

object CodeTracer {

  data class PresetProgram(
      val title: String,
      val description: String,
      val code: String
  )

  val presets: List<PresetProgram> = listOf(
      PresetProgram(
          title = "Factorial Accumulator (#izehwhile)",
          description = "Tracks variable multiplication across while loop iterations.",
          code = """# Factorial Calculation
n = 4
fact = 1
i = 1
while i <= n:
    fact *= i
    i += 1
print("Result:", fact)
"""
      ),
      PresetProgram(
          title = "Variable Swap & Addition (#izehadd)",
          description = "Demonstrates memory allocation and arithmetic evaluation.",
          code = """# tirOtir Addition & Swap
a = 15
b = 25
sum_val = a + b
temp = a
a = b
b = temp
print("Sum:", sum_val)
"""
      ),
      PresetProgram(
          title = "List Item Accumulator (#izehlistops)",
          description = "Iterates through list elements updating running total.",
          code = """# Summing numbers in list
nums = [10, 20, 30]
total = 0
for x in nums:
    total += x
print("Total:", total)
"""
      ),
      PresetProgram(
          title = "Fibonacci Sequence",
          description = "Visualizes dynamic state changes for Fibonacci numbers.",
          code = """# Fibonacci Step Tracer
a = 0
b = 1
count = 0
while count < 4:
    next_val = a + b
    a = b
    b = next_val
    count += 1
print("Done")
"""
      )
  )

  fun trace(code: String): List<TraceStep> {
    val steps = mutableListOf<TraceStep>()
    val lines = code.lines()
    val vars = mutableMapOf<String, Any>()
    var previousVars = mapOf<String, Any>()
    val output = StringBuilder()

    // Add initial state
    steps.add(
        TraceStep(
            stepIndex = 0,
            lineNumber = 1,
            lineCode = lines.firstOrNull()?.trim() ?: "",
            callStack = listOf("<module> (Global)"),
            scope = "Global Scope",
            variables = emptyList(),
            stdout = "",
            explanation = "Program execution started at entry point."
        )
    )

    var currentStep = 1
    for ((idx, rawLine) in lines.withIndex()) {
      val trimmed = rawLine.trim()
      if (trimmed.isEmpty() || trimmed.startsWith("#")) continue

      val lineNum = idx + 1

      // Track variable assignment
      if (trimmed.contains("=") && !trimmed.contains("==") && !trimmed.contains("<=") && !trimmed.contains(">=")) {
        val augOps = listOf("+=", "-=", "*=", "/=")
        var isAug = false
        for (op in augOps) {
          if (trimmed.contains(op)) {
            isAug = true
            val parts = trimmed.split(op)
            val vName = parts[0].trim()
            val delta = parts[1].trim().toIntOrNull() ?: 1
            val curr = (vars[vName] as? Number)?.toInt() ?: 0
            val newVal = when (op) {
              "+=" -> curr + delta
              "-=" -> curr - delta
              "*=" -> curr * delta
              "/=" -> if (delta != 0) curr / delta else curr
              else -> curr + delta
            }
            vars[vName] = newVal
            break
          }
        }

        if (!isAug) {
          val parts = trimmed.split("=", limit = 2)
          val vName = parts[0].trim()
          val expr = parts[1].trim()

          val vVal: Any = when {
            expr.toIntOrNull() != null -> expr.toInt()
            expr.toDoubleOrNull() != null -> expr.toDouble()
            expr.startsWith("\"") || expr.startsWith("'") -> expr.removeSurrounding("\"").removeSurrounding("'")
            expr.startsWith("[") -> expr
            expr.contains("+") -> {
              val p = expr.split("+")
              val l = (vars[p[0].trim()] as? Number)?.toInt() ?: p[0].trim().toIntOrNull() ?: 0
              val r = (vars[p[1].trim()] as? Number)?.toInt() ?: p[1].trim().toIntOrNull() ?: 0
              l + r
            }
            vars.containsKey(expr) -> vars[expr]!!
            else -> expr
          }
          vars[vName] = vVal
        }

        val snapshotList = vars.map { (name, value) ->
          val typeStr = when (value) {
            is Int -> "int"
            is Double -> "float"
            is String -> if (value.startsWith("[")) "list" else "str"
            is Boolean -> "bool"
            else -> "object"
          }
          val prevVal = previousVars[name]
          val changed = prevVal != value
          VariableSnapshot(name, typeStr, value.toString(), changed)
        }

        steps.add(
            TraceStep(
                stepIndex = currentStep++,
                lineNumber = lineNum,
                lineCode = trimmed,
                callStack = listOf("<module> (Global)"),
                scope = "Global Scope",
                variables = snapshotList,
                stdout = output.toString(),
                explanation = "Evaluated line $lineNum: assigned or updated variable state."
            )
        )
        previousVars = HashMap(vars)
      } else if (trimmed.startsWith("print(")) {
        val inside = trimmed.removePrefix("print(").removeSuffix(")")
        output.appendLine(inside.replace("\"", "").replace("'", ""))
        val snapshotList = vars.map { (name, value) ->
          val typeStr = when (value) {
            is Int -> "int"
            is Double -> "float"
            is String -> "str"
            else -> "object"
          }
          VariableSnapshot(name, typeStr, value.toString(), false)
        }
        steps.add(
            TraceStep(
                stepIndex = currentStep++,
                lineNumber = lineNum,
                lineCode = trimmed,
                callStack = listOf("<module> (Global)"),
                scope = "Global Scope",
                variables = snapshotList,
                stdout = output.toString(),
                explanation = "Executed print() to stdout: $inside"
            )
        )
      } else {
        // General statement step
        val snapshotList = vars.map { (name, value) ->
          VariableSnapshot(name, "var", value.toString(), false)
        }
        steps.add(
            TraceStep(
                stepIndex = currentStep++,
                lineNumber = lineNum,
                lineCode = trimmed,
                callStack = listOf("<module> (Global)"),
                scope = "Global Scope",
                variables = snapshotList,
                stdout = output.toString(),
                explanation = "Evaluated line $lineNum condition or statement: $trimmed"
            )
        )
      }
    }

    return steps
  }
}
