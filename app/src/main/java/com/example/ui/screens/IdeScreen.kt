package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ScriptEntity
import com.example.ui.MainViewModel
import com.example.ui.MoreSubDestination
import com.example.ui.components.PythonCodeEditor
import com.example.ui.components.TerminalConsole
import com.example.ui.theme.PythonBlue
import com.example.ui.theme.PythonYellow

@Composable
fun IdeScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
  val code by viewModel.ideCode.collectAsState()
  val output by viewModel.terminalOutput.collectAsState()
  val isSuccess by viewModel.isExecutionSuccess.collectAsState()
  val duration by viewModel.executionDurationMs.collectAsState()
  val isExecuting by viewModel.isExecuting.collectAsState()
  val inputPrompt by viewModel.inputPrompt.collectAsState()
  val savedScripts by viewModel.savedScripts.collectAsState()

  var showSaveDialog by remember { mutableStateOf(false) }
  var showLoadDialog by remember { mutableStateOf(false) }
  var showSnippetsDialog by remember { mutableStateOf(false) }
  var saveScriptTitle by remember { mutableStateOf("") }

  // Save Dialog
  if (showSaveDialog) {
    AlertDialog(
        onDismissRequest = { showSaveDialog = false },
        title = { Text("Save Python Script") },
        text = {
          OutlinedTextField(
              value = saveScriptTitle,
              onValueChange = { saveScriptTitle = it },
              label = { Text("Script Title (e.g. My Calculator)") },
              singleLine = true,
              modifier = Modifier.fillMaxWidth()
          )
        },
        confirmButton = {
          Button(
              onClick = {
                if (saveScriptTitle.isNotBlank()) {
                  viewModel.saveCurrentScript(saveScriptTitle.trim())
                  saveScriptTitle = ""
                  showSaveDialog = false
                }
              }
          ) {
            Text("Save to Room DB")
          }
        },
        dismissButton = {
          TextButton(onClick = { showSaveDialog = false }) {
            Text("Cancel")
          }
        }
    )
  }

  // Load Scripts Dialog
  if (showLoadDialog) {
    AlertDialog(
        onDismissRequest = { showLoadDialog = false },
        title = { Text("Saved Scripts Manager") },
        text = {
          if (savedScripts.isEmpty()) {
            Text(
                text = "No saved scripts found. Save custom scripts from the IDE to persist them locally.",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          } else {
            LazyColumn(
                modifier = Modifier.height(250.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              items(savedScripts) { script ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                          viewModel.updateIdeCode(script.code)
                          showLoadDialog = false
                        },
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                  Row(
                      modifier = Modifier
                          .fillMaxWidth()
                          .padding(12.dp),
                      horizontalArrangement = Arrangement.SpaceBetween,
                      verticalAlignment = Alignment.CenterVertically
                  ) {
                    Column(modifier = Modifier.weight(1f)) {
                      Text(
                          text = script.title,
                          fontWeight = FontWeight.Bold,
                          fontSize = 14.sp
                      )
                      Text(
                          text = "${script.code.lines().size} lines",
                          fontSize = 11.sp,
                          color = MaterialTheme.colorScheme.onSurfaceVariant
                      )
                    }
                    IconButton(onClick = { viewModel.deleteScript(script.id) }) {
                      Icon(
                          imageVector = Icons.Default.DeleteOutline,
                          contentDescription = "Delete",
                          tint = Color(0xFFEF4444)
                      )
                    }
                  }
                }
              }
            }
          }
        },
        confirmButton = {
          TextButton(onClick = { showLoadDialog = false }) {
            Text("Close")
          }
        }
    )
  }

  // Curriculum Snippets Dialog
  if (showSnippetsDialog) {
    AlertDialog(
        onDismissRequest = { showSnippetsDialog = false },
        title = { Text("Academy Code Snippets") },
        text = {
          val snippets = listOf(
              "Hello World (#izeh)" to """# Print Hello World
print("Hello, World!")
print("Welcome to tirOtir AI Academy")
""",
              "Sum & Average (#izehadd)" to """# Addition of Two Numbers
a = 15
b = 25
sum_ab = a + b
print(f"Sum: {sum_ab}")
""",
              "Interactive Input & Convert" to """# User Input Demonstration
name = input("Enter your name: ")
age = int(input("Enter your age: "))
print(f"Hello {name}, in 5 years you will be {age + 5}!")
""",
              "List Operations (#izehlistops)" to """my_list = [1, 2, 3]
my_list.append(4)
my_list.append(5)
print("List:", my_list)
print("Sum:", sum(my_list))
""",
              "Dictionary Mapping (#izehdict)" to """academy = {"name": "tirOtir", "city": "Izeh", "code": 3.12}
print("Academy:", academy["name"])
for k, v in academy.items():
    print(f"{k} => {v}")
""",
              "While Loop Factorial (#izehwhile)" to """count = 1
factorial = 1
number = 5
while count <= number:
    factorial *= count
    count += 1
print(f"Factorial of {number} is {factorial}")
"""
          )

          LazyColumn(
              modifier = Modifier.height(260.dp),
              verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            items(snippets) { (title, snipCode) ->
              Card(
                  modifier = Modifier
                      .fillMaxWidth()
                      .clickable {
                        viewModel.updateIdeCode(snipCode)
                        showSnippetsDialog = false
                      },
                  shape = RoundedCornerShape(10.dp),
                  colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
              ) {
                Column(modifier = Modifier.padding(10.dp)) {
                  Text(
                      text = title,
                      fontWeight = FontWeight.Bold,
                      fontSize = 13.sp,
                      color = PythonYellow
                  )
                  Text(
                      text = snipCode.lines().firstOrNull() ?: "",
                      fontSize = 11.sp,
                      fontFamily = FontFamily.Monospace,
                      color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
              }
            }
          }
        },
        confirmButton = {
          TextButton(onClick = { showSnippetsDialog = false }) {
            Text("Close")
          }
        }
    )
  }

  LazyColumn(
      modifier = modifier
          .fillMaxSize()
          .testTag("ide_screen"),
      contentPadding = PaddingValues(horizontal = 14.dp, vertical = 14.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    // Action bar buttons
    item {
      Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
      ) {
        // Run Code Button
        Button(
            onClick = { viewModel.runIdeCode() },
            colors = ButtonDefaults.buttonColors(
                containerColor = PythonBlue,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.testTag("run_code_button")
        ) {
          if (isExecuting) {
            CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                color = Color.White,
                strokeWidth = 2.dp
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("Running...")
          } else {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("Run Code", fontWeight = FontWeight.Bold)
          }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          // Step in Tracer Button
          OutlinedButton(
              onClick = {
                viewModel.loadTracer(code)
                viewModel.navigateToMoreSub(MoreSubDestination.TRACER)
              },
              shape = RoundedCornerShape(12.dp)
          ) {
            Icon(
                imageVector = Icons.Default.AutoGraph,
                contentDescription = null,
                tint = Color(0xFFA855F7),
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Trace", fontSize = 12.sp)
          }

          // Snippets button
          OutlinedButton(
              onClick = { showSnippetsDialog = true },
              shape = RoundedCornerShape(12.dp)
          ) {
            Icon(
                imageVector = Icons.Default.LibraryBooks,
                contentDescription = null,
                tint = PythonYellow,
                modifier = Modifier.size(16.dp)
            )
          }

          // Save script button
          OutlinedButton(
              onClick = { showSaveDialog = true },
              shape = RoundedCornerShape(12.dp)
          ) {
            Icon(
                imageVector = Icons.Default.Save,
                contentDescription = null,
                tint = Color(0xFF10B981),
                modifier = Modifier.size(16.dp)
            )
          }

          // Load script button
          OutlinedButton(
              onClick = { showLoadDialog = true },
              shape = RoundedCornerShape(12.dp)
          ) {
            Icon(
                imageVector = Icons.Default.FolderOpen,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
          }
        }
      }
    }

    // Python Code Editor with line numbers and symbol bar
    item {
      PythonCodeEditor(
          code = code,
          onCodeChange = { viewModel.updateIdeCode(it) }
      )
    }

    // Output Terminal Console
    item {
      TerminalConsole(
          output = output,
          isSuccess = isSuccess,
          executionTimeMs = duration,
          onClear = { viewModel.clearTerminal() },
          inputPrompt = inputPrompt,
          onInputSubmit = { viewModel.submitInput(it) }
      )
    }

    item {
      Spacer(modifier = Modifier.height(70.dp))
    }
  }
}
