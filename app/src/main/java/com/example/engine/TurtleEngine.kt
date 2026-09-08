package com.example.engine

import androidx.compose.ui.graphics.Color
import kotlin.math.cos
import kotlin.math.sin

data class TurtleLine(
    val startX: Float,
    val startY: Float,
    val endX: Float,
    val endY: Float,
    val color: Color,
    val strokeWidth: Float
)

data class TurtleState(
    val x: Float = 0f,
    val y: Float = 0f,
    val heading: Float = 0f, // 0 = right (East), 90 = down (South), etc.
    val isPenDown: Boolean = true,
    val penColor: Color = Color(0xFF38BDF8),
    val strokeWidth: Float = 3f
)

object TurtleEngine {

  data class TurtleTemplate(
      val name: String,
      val description: String,
      val code: String
  )

  val templates: List<TurtleTemplate> = listOf(
      TurtleTemplate(
          name = "Square (#izehturtule)",
          description = "Classic 4-sided square with 90° right turns.",
          code = """# tirOtir Turtle Square
color("cyan")
for _ in range(4):
    forward(120)
    right(90)
"""
      ),
      TurtleTemplate(
          name = "5-Pointed Star (#izehturtule)",
          description = "Classic geometric star with 144° turns.",
          code = """# tirOtir 5-Pointed Star
color("yellow")
for _ in range(5):
    forward(150)
    right(144)
"""
      ),
      TurtleTemplate(
          name = "Equilateral Triangle",
          description = "3 sides with 120° angles.",
          code = """# Turtle Triangle
color("green")
for _ in range(3):
    forward(140)
    right(120)
"""
      ),
      TurtleTemplate(
          name = "Rainbow Spiral",
          description = "Dynamic expanding spiral with color transitions.",
          code = """# Rainbow Spiral Pattern
for i in range(45):
    forward(i * 4)
    right(59)
"""
      ),
      TurtleTemplate(
          name = "Mandala Circles",
          description = "Floral geometric mandala using rotated circle arcs.",
          code = """# Mandala Pattern
color("magenta")
for _ in range(12):
    circle(40)
    right(30)
"""
      ),
      TurtleTemplate(
          name = "Hexagonal Web",
          description = "Interlocking hexagon spirals.",
          code = """# Hexagon Spiral
color("orange")
for i in range(30):
    forward(i * 5)
    right(60)
"""
      )
  )

  fun parseAndRun(code: String): Pair<List<TurtleLine>, TurtleState> {
    val lines = mutableListOf<TurtleLine>()
    var state = TurtleState()

    val rawLines = code.lines()
    var i = 0
    while (i < rawLines.size) {
      val raw = rawLines[i].trim()
      if (raw.isEmpty() || raw.startsWith("#") || raw.startsWith("import") || raw.contains("turtle.done")) {
        i++
        continue
      }

      // Loop handling: for i in range(N):
      if (raw.startsWith("for ") && raw.contains(" in range(") && raw.endsWith(":")) {
        val rangeStr = raw.substringAfter("range(").substringBefore(")")
        val count = rangeStr.trim().toIntOrNull() ?: 4
        val loopBody = mutableListOf<String>()
        i++
        while (i < rawLines.size) {
          val next = rawLines[i]
          if (next.isNotBlank() && !next.startsWith(" ") && !next.startsWith("\t")) break
          loopBody.add(next.trim())
          i++
        }

        for (step in 0 until count) {
          for (cmd in loopBody) {
            val evaluatedCmd = if (cmd.contains("i *")) {
              val factor = cmd.substringAfter("i *").substringBefore(")").trim().toFloatOrNull() ?: 3f
              val substituted = (step * factor).toInt()
              cmd.replace("i * $factor", "$substituted").replace("i * ${factor.toInt()}", "$substituted")
            } else if (cmd.contains("(i)")) {
              cmd.replace("(i)", "(${step * 3})")
            } else {
              cmd
            }
            state = applyCommand(evaluatedCmd, state, lines, step)
          }
        }
        continue
      }

      state = applyCommand(raw, state, lines, 0)
      i++
    }

    return Pair(lines, state)
  }

  private fun applyCommand(
      cmd: String,
      currentState: TurtleState,
      lines: MutableList<TurtleLine>,
      iterationIndex: Int = 0
  ): TurtleState {
    val clean = cmd.trim().removePrefix("skk.").removePrefix("turtle.")

    // forward(d) / fd(d)
    if (clean.startsWith("forward(") || clean.startsWith("fd(")) {
      val distStr = clean.substringAfter("(").substringBefore(")").trim()
      val dist = distStr.toFloatOrNull() ?: 50f
      val rad = Math.toRadians(currentState.heading.toDouble())
      val newX = currentState.x + (dist * cos(rad)).toFloat()
      val newY = currentState.y + (dist * sin(rad)).toFloat()

      if (currentState.isPenDown) {
        val strokeColor = if (currentState.penColor == Color(0xFF38BDF8) && iterationIndex > 0) {
          // Dynamic gradient for spirals
          val hue = (iterationIndex * 12f) % 360f
          Color.hsl(hue, 0.9f, 0.65f)
        } else {
          currentState.penColor
        }
        lines.add(
            TurtleLine(
                startX = currentState.x,
                startY = currentState.y,
                endX = newX,
                endY = newY,
                color = strokeColor,
                strokeWidth = currentState.strokeWidth
            )
        )
      }
      return currentState.copy(x = newX, y = newY)
    }

    // backward(d) / bk(d)
    if (clean.startsWith("backward(") || clean.startsWith("bk(")) {
      val distStr = clean.substringAfter("(").substringBefore(")").trim()
      val dist = distStr.toFloatOrNull() ?: 50f
      val rad = Math.toRadians(currentState.heading.toDouble())
      val newX = currentState.x - (dist * cos(rad)).toFloat()
      val newY = currentState.y - (dist * sin(rad)).toFloat()

      if (currentState.isPenDown) {
        lines.add(
            TurtleLine(
                startX = currentState.x,
                startY = currentState.y,
                endX = newX,
                endY = newY,
                color = currentState.penColor,
                strokeWidth = currentState.strokeWidth
            )
        )
      }
      return currentState.copy(x = newX, y = newY)
    }

    // right(angle) / rt(angle)
    if (clean.startsWith("right(") || clean.startsWith("rt(")) {
      val angleStr = clean.substringAfter("(").substringBefore(")").trim()
      val angle = angleStr.toFloatOrNull() ?: 90f
      return currentState.copy(heading = (currentState.heading + angle) % 360f)
    }

    // left(angle) / lt(angle)
    if (clean.startsWith("left(") || clean.startsWith("lt(")) {
      val angleStr = clean.substringAfter("(").substringBefore(")").trim()
      val angle = angleStr.toFloatOrNull() ?: 90f
      return currentState.copy(heading = (currentState.heading - angle + 360f) % 360f)
    }

    // penup() / up()
    if (clean.startsWith("penup()") || clean == "up()") {
      return currentState.copy(isPenDown = false)
    }

    // pendown() / down()
    if (clean.startsWith("pendown()") || clean == "down()") {
      return currentState.copy(isPenDown = true)
    }

    // color(c) / pencolor(c)
    if (clean.startsWith("color(") || clean.startsWith("pencolor(")) {
      val colorArg = clean.substringAfter("(").substringBefore(")").replace("\"", "").replace("'", "").trim().lowercase()
      val newColor = when (colorArg) {
        "red" -> Color(0xFFEF4444)
        "green" -> Color(0xFF10B981)
        "blue" -> Color(0xFF3B82F6)
        "yellow" -> Color(0xFFFFD43B)
        "cyan" -> Color(0xFF06B6D4)
        "magenta" -> Color(0xFFD946EF)
        "orange" -> Color(0xFFF97316)
        "white" -> Color.White
        "black" -> Color.Black
        else -> Color(0xFF38BDF8)
      }
      return currentState.copy(penColor = newColor)
    }

    // pensize(w) / width(w)
    if (clean.startsWith("pensize(") || clean.startsWith("width(")) {
      val wStr = clean.substringAfter("(").substringBefore(")").trim()
      val w = wStr.toFloatOrNull() ?: 3f
      return currentState.copy(strokeWidth = w)
    }

    // circle(r)
    if (clean.startsWith("circle(")) {
      val rStr = clean.substringAfter("(").substringBefore(")").trim()
      val radius = rStr.toFloatOrNull() ?: 40f
      var tempState = currentState
      val segments = 24
      val stepDist = (2 * Math.PI * radius / segments).toFloat()
      val stepAngle = 360f / segments
      for (s in 0 until segments) {
        tempState = applyCommand("forward($stepDist)", tempState, lines, iterationIndex)
        tempState = applyCommand("right($stepAngle)", tempState, lines, iterationIndex)
      }
      return tempState
    }

    // reset() / clear()
    if (clean.startsWith("reset()") || clean.startsWith("clear()")) {
      lines.clear()
      return TurtleState()
    }

    return currentState
  }
}
