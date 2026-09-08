package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CodeBg
import com.example.ui.theme.PythonYellow

@Composable
fun TerminalConsole(
    output: String,
    isSuccess: Boolean,
    executionTimeMs: Long,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
    inputPrompt: String? = null,
    onInputSubmit: (String) -> Unit = {}
) {
  val clipboardManager = LocalClipboardManager.current
  var inputDialogOpen by remember(inputPrompt) { mutableStateOf(inputPrompt != null) }
  var userInputValue by remember { mutableStateOf("") }

  // Interactive input() dialog
  if (inputDialogOpen && inputPrompt != null) {
    AlertDialog(
        onDismissRequest = { /* mandatory input */ },
        title = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Terminal,
                contentDescription = null,
                tint = PythonYellow
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Python input() Prompt")
          }
        },
        text = {
          Column {
            Text(
                text = inputPrompt,
                fontFamily = FontFamily.Monospace,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = userInputValue,
                onValueChange = { userInputValue = it },
                label = { Text("Enter your response") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("terminal_input_field")
            )
          }
        },
        confirmButton = {
          Button(
              onClick = {
                val valToSend = userInputValue
                userInputValue = ""
                inputDialogOpen = false
                onInputSubmit(valToSend)
              },
              modifier = Modifier.testTag("submit_input_button")
          ) {
            Text("Submit")
          }
        }
    )
  }

  Column(
      modifier = modifier
          .fillMaxWidth()
          .background(Color(0xFF0B0F19), RoundedCornerShape(16.dp))
          .testTag("terminal_console")
  ) {
    // Header
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF131A29))
            .padding(horizontal = 14.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Default.Terminal,
            contentDescription = "Console",
            tint = Color(0xFF38BDF8),
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Terminal Output",
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFE2E8F0)
        )
        if (executionTimeMs > 0) {
          Spacer(modifier = Modifier.width(8.dp))
          Surface(
              shape = CircleShape,
              color = Color(0xFF1E293B)
          ) {
            Text(
                text = "${executionTimeMs}ms",
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                color = Color(0xFF94A3B8),
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
        }
      }

      Row(verticalAlignment = Alignment.CenterVertically) {
        // Status Pill
        if (output.isNotEmpty()) {
          Surface(
              shape = RoundedCornerShape(6.dp),
              color = if (isSuccess) Color(0x2210B981) else Color(0x22EF4444)
          ) {
            Row(
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                  imageVector = if (isSuccess) Icons.Default.CheckCircle else Icons.Default.Error,
                  contentDescription = null,
                  tint = if (isSuccess) Color(0xFF10B981) else Color(0xFFEF4444),
                  modifier = Modifier.size(12.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                  text = if (isSuccess) "EXIT 0" else "ERROR",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (isSuccess) Color(0xFF10B981) else Color(0xFFEF4444)
              )
            }
          }
        }

        // Copy button
        IconButton(
            onClick = { clipboardManager.setText(AnnotatedString(output)) },
            modifier = Modifier.size(32.dp)
        ) {
          Icon(
              imageVector = Icons.Default.ContentCopy,
              contentDescription = "Copy Output",
              tint = Color(0xFF94A3B8),
              modifier = Modifier.size(16.dp)
          )
        }

        // Clear button
        IconButton(
            onClick = onClear,
            modifier = Modifier.size(32.dp)
        ) {
          Icon(
              imageVector = Icons.Default.Clear,
              contentDescription = "Clear Terminal",
              tint = Color(0xFF94A3B8),
              modifier = Modifier.size(16.dp)
          )
        }
      }
    }

    // Output Body
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 120.dp, max = 220.dp)
            .padding(12.dp)
            .verticalScroll(rememberScrollState())
            .horizontalScroll(rememberScrollState())
    ) {
      if (output.isEmpty()) {
        Text(
            text = "Terminal ready. Press 'Run Code' to execute your Python script.",
            color = Color(0xFF475569),
            fontFamily = FontFamily.Monospace,
            fontSize = 12.sp
        )
      } else {
        Text(
            text = output,
            color = if (isSuccess) Color(0xFFE2E8F0) else Color(0xFFFCA5A5),
            fontFamily = FontFamily.Monospace,
            fontSize = 13.sp,
            lineHeight = 18.sp
        )
      }
    }
  }
}
