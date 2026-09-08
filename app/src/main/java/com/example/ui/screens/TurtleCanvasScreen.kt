package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.TurtleEngine
import com.example.ui.MainViewModel
import com.example.ui.MoreSubDestination
import com.example.ui.components.PythonCodeEditor
import com.example.ui.theme.CodeBg
import com.example.ui.theme.PythonBlue
import com.example.ui.theme.PythonYellow

@Composable
fun TurtleCanvasScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
  val turtleCode by viewModel.turtleCode.collectAsState()
  val lines by viewModel.turtleLines.collectAsState()
  val state by viewModel.turtleState.collectAsState()

  var showTemplatesMenu by remember { mutableStateOf(false) }

  Column(
      modifier = modifier
          .fillMaxSize()
          .testTag("turtle_canvas_screen")
  ) {
    // Top Bar
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
      Row(
          modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 14.dp, vertical = 10.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(onClick = { viewModel.navigateToMoreSub(MoreSubDestination.MENU) }) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
          }
          Spacer(modifier = Modifier.width(4.dp))
          Column {
            Text(
                text = "Interactive Turtle Graphics",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Python 2D Vector Geometry Simulation",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        Box {
          OutlinedButton(
              onClick = { showTemplatesMenu = true },
              shape = RoundedCornerShape(10.dp)
          ) {
            Icon(Icons.Default.Draw, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Patterns", fontSize = 12.sp)
          }
          DropdownMenu(
              expanded = showTemplatesMenu,
              onDismissRequest = { showTemplatesMenu = false }
          ) {
            TurtleEngine.templates.forEach { tmpl ->
              DropdownMenuItem(
                  text = { Text(tmpl.name) },
                  onClick = {
                    viewModel.updateTurtleCode(tmpl.code)
                    viewModel.runTurtle()
                    showTemplatesMenu = false
                  }
              )
            }
          }
        }
      }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // 2D Drawing Canvas Card
      item {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
            border = CardDefaults.outlinedCardBorder()
        ) {
          Box(modifier = Modifier.fillMaxSize()) {
            Canvas(modifier = Modifier.fillMaxSize()) {
              val centerX = size.width / 2f
              val centerY = size.height / 2f

              // Draw subtle coordinate grid
              val gridStep = 40f
              var x = centerX % gridStep
              while (x < size.width) {
                drawLine(
                    color = Color(0xFF1E293B),
                    start = Offset(x, 0f),
                    end = Offset(x, size.height),
                    strokeWidth = 1f
                )
                x += gridStep
              }
              var y = centerY % gridStep
              while (y < size.height) {
                drawLine(
                    color = Color(0xFF1E293B),
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 1f
                )
                y += gridStep
              }

              // Draw turtle lines
              for (line in lines) {
                drawLine(
                    color = line.color,
                    start = Offset(centerX + line.startX, centerY + line.startY),
                    end = Offset(centerX + line.endX, centerY + line.endY),
                    strokeWidth = line.strokeWidth,
                    cap = StrokeCap.Round
                )
              }

              // Draw turtle position point
              val currentTurtleOffset = Offset(centerX + state.x, centerY + state.y)
              drawCircle(
                  color = PythonYellow,
                  radius = 6f,
                  center = currentTurtleOffset
              )
            }

            // Turtle stats pill at top right of canvas
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xCC000000),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp)
            ) {
              Row(
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                  verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                    imageVector = Icons.Default.Navigation,
                    contentDescription = null,
                    tint = PythonYellow,
                    modifier = Modifier
                        .size(12.dp)
                        .rotate(state.heading)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "(${state.x.toInt()}, ${state.y.toInt()}) • ${state.heading.toInt()}°",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color.White
                )
              }
            }
          }
        }
      }

      // Actions row
      item {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Button(
              onClick = { viewModel.runTurtle() },
              colors = ButtonDefaults.buttonColors(containerColor = PythonBlue),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.weight(1.5f)
          ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Run Turtle Code", fontWeight = FontWeight.Bold)
          }

          OutlinedButton(
              onClick = { viewModel.resetTurtle() },
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.weight(1f)
          ) {
            Icon(Icons.Default.Clear, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Reset")
          }
        }
      }

      // Turtle script code editor
      item {
        Column {
          Text(
              text = "Turtle Script (Python)",
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.padding(bottom = 6.dp)
          )
          PythonCodeEditor(
              code = turtleCode,
              onCodeChange = { viewModel.updateTurtleCode(it) }
          )
        }
      }

      // Turtle commands reference
      item {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "SUPPORTED TURTLE COMMANDS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = PythonYellow
            )
            Spacer(modifier = Modifier.height(6.dp))
            val cmds = listOf(
                "forward(d) / fd(d)" to "Move forward by distance d pixels",
                "backward(d) / bk(d)" to "Move backward by distance d",
                "right(angle) / rt(a)" to "Turn turtle right by angle degrees",
                "left(angle) / lt(a)" to "Turn turtle left by angle degrees",
                "penup() / pendown()" to "Toggle line drawing during movement",
                "color(\"cyan\")" to "Change pen stroke color (red, green, blue, yellow, etc.)",
                "circle(r)" to "Draw full circle with radius r",
                "for i in range(N):" to "Repeat block instructions N times"
            )
            cmds.forEach { (cmd, desc) ->
              Row(
                  modifier = Modifier
                      .fillMaxWidth()
                      .padding(vertical = 2.dp),
                  horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(
                    text = cmd,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = desc,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(70.dp))
      }
    }
  }
}
