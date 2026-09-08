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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CurriculumData
import com.example.ui.MainDestination
import com.example.ui.MainViewModel
import com.example.ui.MoreSubDestination
import com.example.ui.components.AcademyHeaderBanner
import com.example.ui.theme.PythonBlue
import com.example.ui.theme.PythonYellow

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
  val userProgress by viewModel.userProgress.collectAsState()
  val completedWeeks = userProgress.completedWeeksCsv.split(",").filter { it.isNotBlank() }.toSet()
  val progressPercent = (completedWeeks.size / 16f).coerceIn(0f, 1f)

  LazyColumn(
      modifier = modifier
          .fillMaxSize()
          .padding(horizontal = 16.dp)
          .testTag("home_screen"),
      contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // 1. Academy Hero Banner
    item {
      AcademyHeaderBanner(
          onExploreSyllabus = { viewModel.navigateTo(MainDestination.SYLLABUS) }
      )
    }

    // 2. Learning Progress & Stats Card
    item {
      Card(
          modifier = Modifier.fillMaxWidth().testTag("progress_card"),
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
      ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
          Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                  text = "Curriculum Completion",
                  fontSize = 16.sp,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                  text = "${completedWeeks.size} of 16 Weeks Completed",
                  fontSize = 13.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
            Surface(
                shape = CircleShape,
                color = PythonBlue.copy(alpha = 0.2f)
            ) {
              Text(
                  text = "${(progressPercent * 100).toInt()}%",
                  fontSize = 14.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = PythonYellow,
                  modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))
          LinearProgressIndicator(
              progress = { progressPercent },
              modifier = Modifier
                  .fillMaxWidth()
                  .height(8.dp)
                  .clip(RoundedCornerShape(4.dp)),
              color = PythonYellow,
              trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
          )

          Spacer(modifier = Modifier.height(16.dp))
          Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceAround
          ) {
            StatPill(
                label = "Score",
                value = "${userProgress.quizScore} pts",
                icon = Icons.Default.AutoGraph
            )
            StatPill(
                label = "Lab Runs",
                value = "${userProgress.labRunsCount}",
                icon = Icons.Default.Terminal
            )
            StatPill(
                label = "Diploma",
                value = if (completedWeeks.size >= 16) "Ready" else "In Progress",
                icon = Icons.Default.WorkspacePremium
            )
          }
        }
      }
    }

    // 3. Quick Action Hub
    item {
      Text(
          text = "Interactive Learning Hub",
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onBackground
      )
      Spacer(modifier = Modifier.height(8.dp))
      Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        QuickActionTile(
            title = "Python IDE",
            subtitle = "Live Interpreter",
            icon = Icons.Default.Code,
            color = PythonBlue,
            modifier = Modifier.weight(1f),
            onClick = { viewModel.navigateTo(MainDestination.IDE_LAB) }
        )
        QuickActionTile(
            title = "Code Tracer",
            subtitle = "Memory Visualizer",
            icon = Icons.Default.AutoGraph,
            color = Color(0xFF7C3AED),
            modifier = Modifier.weight(1f),
            onClick = { viewModel.navigateToMoreSub(MoreSubDestination.TRACER) }
        )
      }
      Spacer(modifier = Modifier.height(12.dp))
      Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        QuickActionTile(
            title = "Turtle Canvas",
            subtitle = "Vector Graphics",
            icon = Icons.Default.Draw,
            color = Color(0xFF0D9488),
            modifier = Modifier.weight(1f),
            onClick = { viewModel.navigateToMoreSub(MoreSubDestination.TURTLE) }
        )
        QuickActionTile(
            title = "Quizzes",
            subtitle = "Milestone Tests",
            icon = Icons.Default.Quiz,
            color = Color(0xFFD97706),
            modifier = Modifier.weight(1f),
            onClick = { viewModel.navigateToMoreSub(MoreSubDestination.QUIZZES) }
        )
      }
    }

    // 4. 16-Week Roadmap Overview
    item {
      Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
            text = "16-Week Curriculum Roadmap",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "View All",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = PythonYellow,
            modifier = Modifier
                .clickable { viewModel.navigateTo(MainDestination.SYLLABUS) }
                .padding(4.dp)
        )
      }
    }

    items(CurriculumData.weeks.take(6)) { week ->
      val isDone = completedWeeks.contains(week.weekNumber.toString())
      Card(
          modifier = Modifier
              .fillMaxWidth()
              .clickable { viewModel.navigateTo(MainDestination.SYLLABUS) },
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(
              containerColor = if (isDone)
                PythonBlue.copy(alpha = 0.15f)
              else
                MaterialTheme.colorScheme.surface
          ),
          border = CardDefaults.outlinedCardBorder()
      ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
          Surface(
              shape = CircleShape,
              color = if (isDone) Color(0xFF10B981) else PythonBlue.copy(alpha = 0.2f),
              modifier = Modifier.size(40.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              if (isDone) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
              } else {
                Text(
                    text = "W${week.weekNumber}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = PythonYellow
                )
              }
            }
          }
          Spacer(modifier = Modifier.width(14.dp))
          Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                  text = week.title,
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
              )
            }
            Text(
                text = week.subtitle,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
          Icon(
              imageVector = Icons.Default.ArrowForward,
              contentDescription = "Open Week",
              tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
              modifier = Modifier.size(18.dp)
          )
        }
      }
    }

    // 5. Official Accreditation Footer Card
    item {
      Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f))
      ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
              imageVector = Icons.Default.School,
              contentDescription = null,
              tint = PythonYellow,
              modifier = Modifier.size(28.dp)
          )
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Text(
                text = "Andisheh Pardazan Anzan Institute",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Technical & Vocational Training Organization (TVTO) Accredited",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    }
  }
}

@Composable
private fun StatPill(
    label: String,
    value: String,
    icon: ImageVector
) {
  Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(4.dp)
  ) {
    Icon(
        imageVector = icon,
        contentDescription = null,
        tint = PythonYellow,
        modifier = Modifier.size(18.dp)
    )
    Spacer(modifier = Modifier.width(6.dp))
    Column {
      Text(
          text = value,
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
      )
      Text(
          text = label,
          fontSize = 11.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}

@Composable
private fun QuickActionTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
  Card(
      modifier = modifier
          .height(105.dp)
          .clickable(onClick = onClick)
          .testTag("tile_${title.lowercase().replace(" ", "_")}"),
      shape = RoundedCornerShape(18.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      border = CardDefaults.outlinedCardBorder()
  ) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
      Surface(
          shape = RoundedCornerShape(10.dp),
          color = color.copy(alpha = 0.2f),
          modifier = Modifier.size(34.dp)
      ) {
        Box(contentAlignment = Alignment.Center) {
          Icon(
              imageVector = icon,
              contentDescription = null,
              tint = color,
              modifier = Modifier.size(18.dp)
          )
        }
      }
      Column {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = subtitle,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }
  }
}
