package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.QuizData
import com.example.data.QuizQuestion
import com.example.ui.MainViewModel
import com.example.ui.MoreSubDestination
import com.example.ui.theme.CodeBg
import com.example.ui.theme.PythonBlue
import com.example.ui.theme.PythonYellow

@Composable
fun QuizScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
  val selectedAnswers by viewModel.selectedAnswers.collectAsState()
  val submittedQuizzes by viewModel.submittedQuizzes.collectAsState()
  val userProgress by viewModel.userProgress.collectAsState()

  var selectedFilter by remember { mutableStateOf("All") }

  val filteredQuestions = when (selectedFilter) {
    "Weeks 1-4" -> QuizData.questions.filter { it.weekNumber in 1..4 }
    "Weeks 5-8" -> QuizData.questions.filter { it.weekNumber in 5..8 }
    "Weeks 9-12" -> QuizData.questions.filter { it.weekNumber in 9..12 }
    "Weeks 13-16" -> QuizData.questions.filter { it.weekNumber in 13..16 }
    else -> QuizData.questions
  }

  Column(
      modifier = modifier
          .fillMaxSize()
          .testTag("quiz_screen")
  ) {
    // Top Bar
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
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
                  text = "Milestone Auto-Grader",
                  fontSize = 18.sp,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                  text = "16 Comprehensive Checkpoints",
                  fontSize = 11.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }

          // Points score pill
          Surface(
              shape = RoundedCornerShape(12.dp),
              color = PythonYellow.copy(alpha = 0.2f)
          ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                  imageVector = Icons.Default.EmojiEvents,
                  contentDescription = null,
                  tint = PythonYellow,
                  modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                  text = "${userProgress.quizScore} pts",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  color = PythonYellow
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Filter chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          listOf("All", "Weeks 1-4", "Weeks 5-8", "Weeks 9-12", "Weeks 13-16").forEach { filter ->
            FilterChip(
                selected = selectedFilter == filter,
                onClick = { selectedFilter = filter },
                label = { Text(filter, fontSize = 11.sp) },
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
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      items(filteredQuestions) { question ->
        val isSubmitted = submittedQuizzes.contains(question.id)
        val selectedOption = selectedAnswers[question.id]
        val isCorrect = selectedOption == question.correctIndex

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("quiz_card_${question.id}"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = CardDefaults.outlinedCardBorder()
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            // Card Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
              Surface(
                  shape = RoundedCornerShape(6.dp),
                  color = PythonBlue.copy(alpha = 0.2f)
              ) {
                Text(
                    text = "Week ${question.weekNumber} Milestone",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = PythonYellow,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
              }

              if (isSubmitted) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (isCorrect) Color(0x2210B981) else Color(0x22EF4444)
                ) {
                  Row(
                      modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                      verticalAlignment = Alignment.CenterVertically
                  ) {
                    Icon(
                        imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.Close,
                        contentDescription = null,
                        tint = if (isCorrect) Color(0xFF10B981) else Color(0xFFEF4444),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isCorrect) "+10 pts" else "Incorrect",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isCorrect) Color(0xFF10B981) else Color(0xFFEF4444)
                    )
                  }
                }
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Question Prompt
            Text(
                text = question.question,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 20.sp
            )

            // Code snippet if present
            if (question.codeSnippet != null) {
              Spacer(modifier = Modifier.height(8.dp))
              Surface(
                  shape = RoundedCornerShape(8.dp),
                  color = CodeBg,
                  modifier = Modifier.fillMaxWidth()
              ) {
                Text(
                    text = question.codeSnippet,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    color = Color(0xFFE2E8F0),
                    modifier = Modifier.padding(10.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Options
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
              question.options.forEachIndexed { optIdx, optText ->
                val isThisOptionSelected = selectedOption == optIdx
                val optionBorderColor = when {
                  isSubmitted && optIdx == question.correctIndex -> Color(0xFF10B981)
                  isSubmitted && isThisOptionSelected && !isCorrect -> Color(0xFFEF4444)
                  isThisOptionSelected -> PythonYellow
                  else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = when {
                      isSubmitted && optIdx == question.correctIndex -> Color(0x1A10B981)
                      isSubmitted && isThisOptionSelected && !isCorrect -> Color(0x1AEF4444)
                      isThisOptionSelected -> PythonBlue.copy(alpha = 0.1f)
                      else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    },
                    border = BorderStroke(1.dp, optionBorderColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = !isSubmitted) {
                          viewModel.selectAnswer(question.id, optIdx)
                        }
                ) {
                  Row(
                      modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                      verticalAlignment = Alignment.CenterVertically
                  ) {
                    RadioButton(
                        selected = isThisOptionSelected,
                        onClick = {
                          if (!isSubmitted) viewModel.selectAnswer(question.id, optIdx)
                        },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = PythonYellow,
                            unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = optText,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                  }
                }
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Submit Button
            if (!isSubmitted) {
              Button(
                  onClick = { viewModel.submitQuiz(question) },
                  enabled = selectedOption != null,
                  colors = ButtonDefaults.buttonColors(containerColor = PythonBlue),
                  shape = RoundedCornerShape(10.dp),
                  modifier = Modifier.fillMaxWidth()
              ) {
                Text("Submit Answer", fontWeight = FontWeight.Bold)
              }
            } else {
              // Explanation banner
              Surface(
                  shape = RoundedCornerShape(10.dp),
                  color = if (isCorrect) Color(0x1A10B981) else Color(0x1AEF4444),
                  modifier = Modifier.fillMaxWidth()
              ) {
                Column(modifier = Modifier.padding(12.dp)) {
                  Text(
                      text = if (isCorrect) "✓ Correct Answer!" else "✕ Incorrect",
                      fontSize = 12.sp,
                      fontWeight = FontWeight.Bold,
                      color = if (isCorrect) Color(0xFF10B981) else Color(0xFFEF4444)
                  )
                  Spacer(modifier = Modifier.height(4.dp))
                  Text(
                      text = question.explanation,
                      fontSize = 12.sp,
                      color = MaterialTheme.colorScheme.onSurface,
                      lineHeight = 17.sp
                  )
                }
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
