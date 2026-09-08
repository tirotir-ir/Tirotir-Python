package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.example.data.CurriculumData
import com.example.data.Lesson
import com.example.data.WeekModule
import com.example.ui.MainViewModel
import com.example.ui.components.CodeBlockCard
import com.example.ui.theme.PythonBlue
import com.example.ui.theme.PythonYellow

@Composable
fun CurriculumScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
  val userProgress by viewModel.userProgress.collectAsState()
  val completedWeeks = userProgress.completedWeeksCsv.split(",").filter { it.isNotBlank() }.toSet()
  var expandedWeekIndex by remember { mutableStateOf<Int?>(1) } // Default Week 1 open

  Column(
      modifier = modifier
          .fillMaxSize()
          .testTag("curriculum_screen")
  ) {
    // Top bar header
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
      Column(
          modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp)
      ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
          Column {
            Text(
                text = "16-Week Official Syllabus",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "tirOtir AI Academy • Technical & Vocational Training",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          Surface(
              shape = RoundedCornerShape(12.dp),
              color = PythonBlue.copy(alpha = 0.15f)
          ) {
            Text(
                text = "${completedWeeks.size}/16 Done",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = PythonYellow,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
            )
          }
        }
      }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      items(CurriculumData.weeks) { week ->
        val isExpanded = expandedWeekIndex == week.weekNumber
        val isDone = completedWeeks.contains(week.weekNumber.toString())

        WeekAccordionCard(
            week = week,
            isExpanded = isExpanded,
            isDone = isDone,
            onToggleExpand = {
              expandedWeekIndex = if (isExpanded) null else week.weekNumber
            },
            onToggleDone = {
              viewModel.toggleLessonCompleted(week.weekNumber)
            },
            onRunInLab = { code ->
              viewModel.loadCodeIntoIde(code)
            }
        )
      }
      item {
        Spacer(modifier = Modifier.height(80.dp))
      }
    }
  }
}

@Composable
private fun WeekAccordionCard(
    week: WeekModule,
    isExpanded: Boolean,
    isDone: Boolean,
    onToggleExpand: () -> Unit,
    onToggleDone: () -> Unit,
    onRunInLab: (String) -> Unit
) {
  Card(
      modifier = Modifier
          .fillMaxWidth()
          .testTag("week_card_${week.weekNumber}"),
      shape = RoundedCornerShape(18.dp),
      colors = CardDefaults.cardColors(
          containerColor = if (isDone)
            PythonBlue.copy(alpha = 0.1f)
          else
            MaterialTheme.colorScheme.surface
      ),
      border = CardDefaults.outlinedCardBorder()
  ) {
    Column(modifier = Modifier.fillMaxWidth()) {
      // Header Row
      Row(
          modifier = Modifier
              .fillMaxWidth()
              .clickable(onClick = onToggleExpand)
              .padding(16.dp),
          verticalAlignment = Alignment.CenterVertically
      ) {
        // Complete checkbox
        IconButton(
            onClick = onToggleDone,
            modifier = Modifier.size(36.dp)
        ) {
          Icon(
              imageVector = if (isDone) Icons.Default.CheckCircle else Icons.Default.CheckCircleOutline,
              contentDescription = "Toggle Complete",
              tint = if (isDone) Color(0xFF10B981) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
              modifier = Modifier.size(24.dp)
          )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
          Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Text(
                text = "Week ${week.weekNumber}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = PythonYellow
            )
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
              Text(
                  text = week.badge,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Medium,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }
          Text(
              text = week.title.substringAfter(":").trim(),
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
          )
          Text(
              text = week.subtitle,
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        Icon(
            imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
            contentDescription = if (isExpanded) "Collapse" else "Expand",
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      // Expanded Content
      AnimatedVisibility(visible = isExpanded) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
          HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
          Spacer(modifier = Modifier.height(14.dp))

          week.lessons.forEachIndexed { idx, lesson ->
            LessonDetailView(
                lesson = lesson,
                lessonNumber = idx + 1,
                onRunInLab = onRunInLab
            )
            if (idx < week.lessons.size - 1) {
              Spacer(modifier = Modifier.height(18.dp))
              HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
              Spacer(modifier = Modifier.height(18.dp))
            }
          }
          Spacer(modifier = Modifier.height(8.dp))
        }
      }
    }
  }
}

@Composable
private fun LessonDetailView(
    lesson: Lesson,
    lessonNumber: Int,
    onRunInLab: (String) -> Unit
) {
  var showHint by remember { mutableStateOf(false) }
  var showSolution by remember { mutableStateOf(false) }

  Column(modifier = Modifier.fillMaxWidth()) {
    // Lesson Title
    Row(verticalAlignment = Alignment.CenterVertically) {
      Surface(
          shape = CircleShape,
          color = PythonBlue,
          modifier = Modifier.size(24.dp)
      ) {
        Box(contentAlignment = Alignment.Center) {
          Text(
              text = "$lessonNumber",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
          )
        }
      }
      Spacer(modifier = Modifier.width(8.dp))
      Text(
          text = lesson.title,
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
      )
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Conceptual Explanation
    Text(
        text = lesson.conceptualExplanation,
        fontSize = 13.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        lineHeight = 19.sp
    )

    Spacer(modifier = Modifier.height(12.dp))

    // Syntax Breakdown Box
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(12.dp)) {
        Text(
            text = "SYNTAX RULES",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = PythonYellow
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = lesson.syntaxBreakdown,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 17.sp
        )
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Runnable Example Code Block
    CodeBlockCard(
        code = lesson.runnableCode,
        title = "Runnable Code Example",
        onRunInLab = onRunInLab
    )

    Spacer(modifier = Modifier.height(12.dp))

    // Extra Example (tirOtir snippet)
    CodeBlockCard(
        code = lesson.extraExampleCode,
        title = lesson.extraExampleTitle,
        onRunInLab = onRunInLab
    )

    Spacer(modifier = Modifier.height(14.dp))

    // Practice Exercise Card
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
      Column(modifier = Modifier.padding(14.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
              imageVector = Icons.Default.HelpOutline,
              contentDescription = null,
              tint = PythonYellow,
              modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
              text = "Practical Exercise",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
          )
        }

        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = lesson.exerciseQuestion,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Hint & Solution Controls
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          TextButton(onClick = { showHint = !showHint }) {
            Icon(
                imageVector = Icons.Default.Lightbulb,
                contentDescription = null,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(if (showHint) "Hide Hint" else "View Hint", fontSize = 12.sp)
          }

          TextButton(onClick = { showSolution = !showSolution }) {
            Icon(
                imageVector = Icons.Default.Visibility,
                contentDescription = null,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(if (showSolution) "Hide Solution" else "Reveal Solution", fontSize = 12.sp)
          }
        }

        if (showHint) {
          Surface(
              shape = RoundedCornerShape(8.dp),
              color = Color(0x1AF59E0B),
              modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
          ) {
            Text(
                text = "💡 Hint: ${lesson.exerciseHint}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(8.dp)
            )
          }
        }

        if (showSolution) {
          Surface(
              shape = RoundedCornerShape(8.dp),
              color = Color(0x1A10B981),
              modifier = Modifier.fillMaxWidth().padding(top = 6.dp)
          ) {
            Column(modifier = Modifier.padding(8.dp)) {
              Text(
                  text = "✓ Solution Code:",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF10B981)
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                  text = lesson.exerciseSolution,
                  fontSize = 12.sp,
                  fontFamily = FontFamily.Monospace,
                  color = MaterialTheme.colorScheme.onSurface
              )
            }
          }
        }
      }
    }
  }
}
