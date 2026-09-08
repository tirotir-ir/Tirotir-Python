package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import com.example.data.ProjectData
import com.example.data.ProjectItem
import com.example.ui.MainViewModel
import com.example.ui.components.CodeBlockCard
import com.example.ui.theme.PythonBlue
import com.example.ui.theme.PythonYellow
import kotlin.random.Random

@Composable
fun ProjectsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
  var selectedProjectIndex by remember { mutableStateOf(0) }
  val activeProject = ProjectData.projects[selectedProjectIndex]

  Column(
      modifier = modifier
          .fillMaxSize()
          .testTag("projects_screen")
  ) {
    // Header
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp)) {
        Text(
            text = "Practical Project Suite",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Interactive UI Previews & Complete Source Code",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(10.dp))

        // Project filter chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          ProjectData.projects.forEachIndexed { index, proj ->
            FilterChip(
                selected = selectedProjectIndex == index,
                onClick = { selectedProjectIndex = index },
                label = { Text(proj.title, fontSize = 12.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = PythonBlue,
                    selectedLabelColor = Color.White
                )
            )
          }
        }
      }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Active Project Overview
      item {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                  text = activeProject.title,
                  fontSize = 18.sp,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
              )
              Surface(
                  shape = RoundedCornerShape(6.dp),
                  color = PythonYellow.copy(alpha = 0.2f)
              ) {
                Text(
                    text = "tirOtir Practical",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = PythonYellow,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = activeProject.description,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = { viewModel.loadCodeIntoIde(activeProject.sourceCode) },
                colors = ButtonDefaults.buttonColors(containerColor = PythonBlue),
                shape = RoundedCornerShape(10.dp)
            ) {
              Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Run in Python IDE Lab", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
          }
        }
      }

      // Interactive UI Sandbox for the Project
      item {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = CardDefaults.outlinedCardBorder()
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "INTERACTIVE APP PREVIEW",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = PythonYellow
            )
            Spacer(modifier = Modifier.height(12.dp))

            when (activeProject.id) {
              "calc" -> CalculatorPreview()
              "morse" -> MorseCodePreview()
              "guess" -> NumberGuessingPreview()
              "pass" -> PasswordGeneratorPreview()
              "rps" -> RockPaperScissorsPreview()
              "todo" -> TaskManagerPreview()
              "kg_convert" -> KgConverterPreview()
              else -> Text("Preview available.")
            }
          }
        }
      }

      // Source Code Viewer
      item {
        CodeBlockCard(
            code = activeProject.sourceCode,
            title = "${activeProject.title} (Source Code)",
            onRunInLab = { viewModel.loadCodeIntoIde(it) }
        )
      }

      item {
        Spacer(modifier = Modifier.height(80.dp))
      }
    }
  }
}

// 1. Calculator Live Preview
@Composable
private fun CalculatorPreview() {
  var num1 by remember { mutableStateOf("12") }
  var num2 by remember { mutableStateOf("4") }
  var result by remember { mutableStateOf("16") }

  Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      OutlinedTextField(
          value = num1,
          onValueChange = { num1 = it },
          label = { Text("Num 1") },
          modifier = Modifier.weight(1f)
      )
      OutlinedTextField(
          value = num2,
          onValueChange = { num2 = it },
          label = { Text("Num 2") },
          modifier = Modifier.weight(1f)
      )
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      listOf("+", "-", "*", "/").forEach { op ->
        Button(
            onClick = {
              val n1 = num1.toDoubleOrNull() ?: 0.0
              val n2 = num2.toDoubleOrNull() ?: 0.0
              result = when (op) {
                "+" -> "${n1 + n2}"
                "-" -> "${n1 - n2}"
                "*" -> "${n1 * n2}"
                "/" -> if (n2 != 0.0) "${n1 / n2}" else "Div by Zero"
                else -> ""
              }
            },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = PythonBlue)
        ) {
          Text(op, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
      }
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.fillMaxWidth()
    ) {
      Text(
          text = "Result: $result",
          fontWeight = FontWeight.Bold,
          fontSize = 15.sp,
          color = PythonYellow,
          modifier = Modifier.padding(12.dp)
      )
    }
  }
}

// 2. Morse Code Live Preview
@Composable
private fun MorseCodePreview() {
  var textInput by remember { mutableStateOf("TIROTIR") }
  val morseTable = mapOf(
      'A' to ".-", 'B' to "-...", 'C' to "-.-.", 'D' to "-..", 'E' to ".", 'F' to "..-.",
      'G' to "--.", 'H' to "....", 'I' to "..", 'J' to ".---", 'K' to "-.-", 'L' to ".-..",
      'M' to "--", 'N' to "-.", 'O' to "---", 'P' to ".--.", 'Q' to "--.-", 'R' to ".-.",
      'S' to "...", 'T' to "-", 'U' to "..-", 'V' to "...-", 'W' to ".--", 'X' to "-..-",
      'Y' to "-.--", 'Z' to "--..", ' ' to "/"
  )

  val converted = textInput.uppercase().map { morseTable[it] ?: "?" }.joinToString(" ")

  Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
    OutlinedTextField(
        value = textInput,
        onValueChange = { textInput = it },
        label = { Text("English Text") },
        modifier = Modifier.fillMaxWidth()
    )
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFF0F172A),
        modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(12.dp)) {
        Text("Morse Representation:", fontSize = 11.sp, color = Color(0xFF94A3B8))
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = converted,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = PythonYellow
        )
      }
    }
  }
}

// 3. Number Guessing Game Preview
@Composable
private fun NumberGuessingPreview() {
  var target by remember { mutableStateOf(Random.nextInt(1, 100)) }
  var guessInput by remember { mutableStateOf("") }
  var feedback by remember { mutableStateOf("Guess a number between 1 and 100") }
  var tries by remember { mutableStateOf(0) }

  Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      OutlinedTextField(
          value = guessInput,
          onValueChange = { guessInput = it },
          label = { Text("Your Guess") },
          modifier = Modifier.weight(1f)
      )
      Button(
          onClick = {
            val g = guessInput.toIntOrNull()
            if (g != null) {
              tries++
              feedback = when {
                g < target -> "Too Low! Try higher. (Tries: $tries)"
                g > target -> "Too High! Try lower. (Tries: $tries)"
                else -> "🎉 Correct! Number was $target. Guessed in $tries tries."
              }
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = PythonBlue),
          modifier = Modifier.align(Alignment.CenterVertically)
      ) {
        Text("Check")
      }
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.fillMaxWidth()
    ) {
      Text(
          text = feedback,
          fontSize = 13.sp,
          color = MaterialTheme.colorScheme.onSurface,
          modifier = Modifier.padding(12.dp)
      )
    }
  }
}

// 4. Password Generator Preview
@Composable
private fun PasswordGeneratorPreview() {
  var generatedPass by remember { mutableStateOf("tr0t!r_Pyt#99") }
  var passLength by remember { mutableStateOf("12") }

  Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
      OutlinedTextField(
          value = passLength,
          onValueChange = { passLength = it },
          label = { Text("Length") },
          modifier = Modifier.width(90.dp)
      )
      Button(
          onClick = {
            val len = passLength.toIntOrNull() ?: 12
            val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*"
            generatedPass = (1..len).map { chars.random() }.joinToString("")
          },
          colors = ButtonDefaults.buttonColors(containerColor = PythonBlue)
      ) {
        Text("Generate Password")
      }
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFF0F172A),
        modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(12.dp)) {
        Text("Secure Generated Key:", fontSize = 11.sp, color = Color(0xFF94A3B8))
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = generatedPass,
            fontFamily = FontFamily.Monospace,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF10B981)
        )
      }
    }
  }
}

// 5. Rock Paper Scissors Preview
@Composable
private fun RockPaperScissorsPreview() {
  var botChoice by remember { mutableStateOf("Scissors") }
  var userChoice by remember { mutableStateOf("Rock") }
  var gameOutcome by remember { mutableStateOf("You Win! Rock smashes Scissors.") }

  Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      listOf("Rock", "Paper", "Scissors").forEach { opt ->
        Button(
            onClick = {
              userChoice = opt
              val bot = listOf("Rock", "Paper", "Scissors").random()
              botChoice = bot
              gameOutcome = when {
                userChoice == bot -> "It's a Tie! Both chose $userChoice."
                (userChoice == "Rock" && bot == "Scissors") ||
                    (userChoice == "Paper" && bot == "Rock") ||
                    (userChoice == "Scissors" && bot == "Paper") -> "You Win! $userChoice beats $bot."
                else -> "Computer Wins! $bot beats $userChoice."
              }
            },
            colors = ButtonDefaults.buttonColors(containerColor = PythonBlue),
            modifier = Modifier.weight(1f)
        ) {
          Text(opt, fontSize = 12.sp)
        }
      }
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(12.dp)) {
        Text("User: $userChoice | Computer: $botChoice", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = gameOutcome, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = PythonYellow)
      }
    }
  }
}

// 6. Terminal Task Manager Preview
@Composable
private fun TaskManagerPreview() {
  val tasks = remember { mutableStateListOf("Complete Week 3 Lesson", "Run Turtle Mandala", "Practice While Loops") }
  var newTask by remember { mutableStateOf("") }

  Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      OutlinedTextField(
          value = newTask,
          onValueChange = { newTask = it },
          label = { Text("New Task") },
          modifier = Modifier.weight(1f)
      )
      Button(
          onClick = {
            if (newTask.isNotBlank()) {
              tasks.add(newTask.trim())
              newTask = ""
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = PythonBlue),
          modifier = Modifier.align(Alignment.CenterVertically)
      ) {
        Text("Add")
      }
    }

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
      tasks.forEachIndexed { idx, t ->
        Surface(
            shape = RoundedCornerShape(6.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.fillMaxWidth()
        ) {
          Row(
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
          ) {
            Text("${idx + 1}. $t", fontSize = 12.sp)
            Text(
                text = "✕",
                color = Color(0xFFEF4444),
                modifier = Modifier
                    .clickable { tasks.removeAt(idx) }
                    .padding(4.dp)
            )
          }
        }
      }
    }
  }
}

// 7. Kg Converter Preview (#izehkgcmd / #izehkggtk)
@Composable
private fun KgConverterPreview() {
  var kgInput by remember { mutableStateOf("75") }
  val kg = kgInput.toDoubleOrNull() ?: 0.0
  val grams = kg * 1000
  val pounds = kg * 2.20462
  val ounces = kg * 35.274

  Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
    OutlinedTextField(
        value = kgInput,
        onValueChange = { kgInput = it },
        label = { Text("Weight in Kilograms (kg)") },
        modifier = Modifier.fillMaxWidth()
    )

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFF0F172A),
        modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text("Converted Units:", fontSize = 11.sp, color = Color(0xFF94A3B8))
        Text("• Grams: ${grams.toInt()} g", color = PythonYellow, fontFamily = FontFamily.Monospace)
        Text("• Pounds: ${String.format("%.2f", pounds)} lbs", color = Color.White, fontFamily = FontFamily.Monospace)
        Text("• Ounces: ${String.format("%.2f", ounces)} oz", color = Color(0xFF38BDF8), fontFamily = FontFamily.Monospace)
      }
    }
  }
}
