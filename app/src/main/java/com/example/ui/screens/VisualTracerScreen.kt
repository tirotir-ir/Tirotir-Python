package com.example.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.CodeTracer
import com.example.engine.TraceStep
import com.example.engine.VariableSnapshot
import com.example.ui.MainViewModel
import com.example.ui.MoreSubDestination
import com.example.ui.theme.CodeBg
import com.example.ui.theme.PythonBlue
import com.example.ui.theme.PythonYellow

@Composable
fun VisualTracerScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
  val tracerCode by viewModel.tracerCode.collectAsState()
  val steps by viewModel.traceSteps.collectAsState()
  val currentStepIdx by viewModel.currentStepIndex.collectAsState()
  val isPlaying by viewModel.isTracerPlaying.collectAsState()
  val currentSpeed by viewModel.tracerSpeedMs.collectAsState()

  val currentStep: TraceStep? = steps.getOrNull(currentStepIdx)
  val codeLines = tracerCode.lines()
  var showPresetsMenu by remember { mutableStateOf(false) }

  Column(
      modifier = modifier
          .fillMaxSize()
          .testTag("visual_tracer_screen")
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
                text = "Visual Code Tracer",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Line-by-Line Memory & Stack Visualizer",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        Box {
          OutlinedButton(
              onClick = { showPresetsMenu = true },
              shape = RoundedCornerShape(10.dp)
          ) {
            Text("Presets", fontSize = 12.sp)
          }
          DropdownMenu(
              expanded = showPresetsMenu,
              onDismissRequest = { showPresetsMenu = false }
          ) {
            CodeTracer.presets.forEach { preset ->
              DropdownMenuItem(
                  text = { Text(preset.title) },
                  onClick = {
                    viewModel.loadTracer(preset.code)
                    showPresetsMenu = false
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
      // Step controls & counter
      item {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                  text = "Step ${currentStepIdx + 1} of ${maxOf(steps.size, 1)}",
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Bold,
                  color = PythonYellow
              )

              // Speed selector
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Speed,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                listOf(1500L to "Slow", 800L to "Norm", 300L to "Fast").forEach { (speed, label) ->
                  val isSelected = currentSpeed == speed
                  Surface(
                      shape = RoundedCornerShape(6.dp),
                      color = if (isSelected) PythonBlue else Color.Transparent,
                      modifier = Modifier
                          .clickable { viewModel.setTracerSpeed(speed) }
                          .padding(horizontal = 4.dp)
                  ) {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                  }
                }
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Playback buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              OutlinedButton(
                  onClick = { viewModel.resetTracer() },
                  shape = RoundedCornerShape(10.dp),
                  modifier = Modifier.weight(1f)
              ) {
                Icon(Icons.Default.Refresh, contentDescription = "Reset", modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Reset", fontSize = 12.sp)
              }

              Button(
                  onClick = { viewModel.togglePlayTracer() },
                  colors = ButtonDefaults.buttonColors(
                      containerColor = if (isPlaying) Color(0xFFEF4444) else PythonBlue
                  ),
                  shape = RoundedCornerShape(10.dp),
                  modifier = Modifier.weight(1.2f)
              ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(if (isPlaying) "Pause" else "Auto Play", fontSize = 12.sp)
              }

              Button(
                  onClick = { viewModel.stepForwardTracer() },
                  colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F766E)),
                  shape = RoundedCornerShape(10.dp),
                  modifier = Modifier.weight(1.2f)
              ) {
                Icon(Icons.Default.SkipNext, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Next Step", fontSize = 12.sp)
              }
            }
          }
        }
      }

      // Code View with Line Highlighting
      item {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CodeBg)
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "EXECUTION POINTER",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF64748B),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            codeLines.forEachIndexed { idx, lineText ->
              val lineNum = idx + 1
              val isCurrentLine = currentStep?.lineNumber == lineNum

              val bgColor = if (isCurrentLine) PythonYellow.copy(alpha = 0.2f) else Color.Transparent
              val textColor = if (isCurrentLine) PythonYellow else Color(0xFFE2E8F0)

              Row(
                  modifier = Modifier
                      .fillMaxWidth()
                      .background(bgColor, RoundedCornerShape(6.dp))
                      .padding(vertical = 3.dp, horizontal = 6.dp),
                  verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                    text = if (isCurrentLine) "▶ $lineNum" else "  $lineNum",
                    color = if (isCurrentLine) PythonYellow else Color(0xFF475569),
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.width(36.dp)
                )
                Text(
                    text = lineText,
                    color = textColor,
                    fontSize = 13.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = if (isCurrentLine) FontWeight.Bold else FontWeight.Normal
                )
              }
            }
          }
        }
      }

      // Call Stack & Scope
      item {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Card(
              modifier = Modifier.weight(1f),
              shape = RoundedCornerShape(12.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
          ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                  imageVector = Icons.Default.Layers,
                  contentDescription = null,
                  tint = Color(0xFF38BDF8),
                  modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Text("Call Stack", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(
                    text = currentStep?.callStack?.lastOrNull() ?: "<module>",
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
              }
            }
          }

          Card(
              modifier = Modifier.weight(1f),
              shape = RoundedCornerShape(12.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
          ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                  imageVector = Icons.Default.AutoGraph,
                  contentDescription = null,
                  tint = Color(0xFFA855F7),
                  modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Text("Active Scope", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(
                    text = currentStep?.scope ?: "Global",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
              }
            }
          }
        }
      }

      // Visual Memory State (Variables Grid)
      item {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = CardDefaults.outlinedCardBorder()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                  imageVector = Icons.Default.Memory,
                  contentDescription = null,
                  tint = PythonYellow,
                  modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                  text = "Active Memory State",
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
              )
            }

            Spacer(modifier = Modifier.height(10.dp))

            val vars = currentStep?.variables ?: emptyList()
            if (vars.isEmpty()) {
              Text(
                  text = "No variables in scope yet.",
                  fontSize = 12.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            } else {
              Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                vars.forEach { v ->
                  VariableMemoryCard(snapshot = v)
                }
              }
            }
          }
        }
      }

      // Step Explanation & Output
      item {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "Step Explanation",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = currentStep?.explanation ?: "Program started.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp
            )

            if (!currentStep?.stdout.isNullOrEmpty()) {
              Spacer(modifier = Modifier.height(10.dp))
              HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                  text = "Accumulated stdout:",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = Color(0xFF10B981)
              )
              Text(
                  text = currentStep?.stdout ?: "",
                  fontSize = 12.sp,
                  fontFamily = FontFamily.Monospace,
                  color = MaterialTheme.colorScheme.onSurface
              )
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

@Composable
private fun VariableMemoryCard(snapshot: VariableSnapshot) {
  val borderColor = if (snapshot.isChanged) PythonYellow else Color.Transparent

  Surface(
      shape = RoundedCornerShape(10.dp),
      color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
      modifier = Modifier
          .fillMaxWidth()
          .border(width = if (snapshot.isChanged) 1.5.dp else 0.dp, color = borderColor, shape = RoundedCornerShape(10.dp))
  ) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = snapshot.name,
            fontFamily = FontFamily.Monospace,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(8.dp))
        Surface(
            shape = RoundedCornerShape(6.dp),
            color = Color(0xFF334155)
        ) {
          Text(
              text = snapshot.type,
              fontSize = 10.sp,
              fontFamily = FontFamily.Monospace,
              color = Color(0xFF38BDF8),
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }
      }

      Row(verticalAlignment = Alignment.CenterVertically) {
        if (snapshot.isChanged) {
          Text(
              text = "UPDATED",
              fontSize = 9.sp,
              fontWeight = FontWeight.Bold,
              color = PythonYellow,
              modifier = Modifier.padding(end = 6.dp)
          )
        }
        Text(
            text = snapshot.value,
            fontFamily = FontFamily.Monospace,
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold,
            color = PythonYellow
        )
      }
    }
  }
}
