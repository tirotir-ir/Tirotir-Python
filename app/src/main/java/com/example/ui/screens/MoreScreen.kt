package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainViewModel
import com.example.ui.MoreSubDestination
import com.example.ui.theme.AppThemeMode
import com.example.ui.theme.PythonBlue
import com.example.ui.theme.PythonYellow

@Composable
fun MoreScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
  val subDestination by viewModel.moreSubDestination.collectAsState()

  when (subDestination) {
    MoreSubDestination.TRACER -> VisualTracerScreen(viewModel = viewModel, modifier = modifier)
    MoreSubDestination.TURTLE -> TurtleCanvasScreen(viewModel = viewModel, modifier = modifier)
    MoreSubDestination.QUIZZES -> QuizScreen(viewModel = viewModel, modifier = modifier)
    MoreSubDestination.CERTIFICATE -> CertificateScreen(viewModel = viewModel, modifier = modifier)
    MoreSubDestination.SETTINGS -> SettingsSubScreen(viewModel = viewModel, modifier = modifier)
    MoreSubDestination.MENU -> MoreMenuHub(viewModel = viewModel, modifier = modifier)
  }
}

@Composable
private fun MoreMenuHub(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
  val themeMode by viewModel.themeMode.collectAsState()

  LazyColumn(
      modifier = modifier
          .fillMaxSize()
          .testTag("more_screen"),
      contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Header
    item {
      Column {
        Text(
            text = "Academy Tools & Settings",
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Advanced visualizers, quizzes, credentials, and configuration",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    // Theme selector card (System, Light, Dark, Sepia)
    item {
      Card(
          modifier = Modifier.fillMaxWidth().testTag("theme_selector_card"),
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.ColorLens, contentDescription = null, tint = PythonYellow, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Display Theme (M3 & Eye Comfort)",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
          }
          Spacer(modifier = Modifier.height(10.dp))
          Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            listOf(
                AppThemeMode.DARK to "Dark",
                AppThemeMode.LIGHT to "Light",
                AppThemeMode.SEPIA to "Sepia",
                AppThemeMode.SYSTEM to "Auto"
            ).forEach { (mode, label) ->
              FilterChip(
                  selected = themeMode == mode,
                  onClick = { viewModel.setTheme(mode) },
                  label = { Text(label, fontSize = 12.sp) },
                  colors = FilterChipDefaults.filterChipColors(
                      selectedContainerColor = PythonBlue,
                      selectedLabelColor = Color.White
                  ),
                  modifier = Modifier.weight(1f)
              )
            }
          }
        }
      }
    }

    // Tools List
    item {
      Text(
          text = "Interactive Modules",
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }

    item {
      MoreNavTile(
          title = "Visual Code Tracer",
          subtitle = "Step-by-step memory stack and variable changes",
          icon = Icons.Default.AutoGraph,
          iconColor = Color(0xFFA855F7),
          onClick = { viewModel.navigateToMoreSub(MoreSubDestination.TRACER) }
      )
    }

    item {
      MoreNavTile(
          title = "Turtle Graphics Canvas",
          subtitle = "Interactive 2D vector geometry drawing laboratory",
          icon = Icons.Default.Draw,
          iconColor = Color(0xFF0D9488),
          onClick = { viewModel.navigateToMoreSub(MoreSubDestination.TURTLE) }
      )
    }

    item {
      MoreNavTile(
          title = "16-Milestone Quizzes",
          subtitle = "Self-assessment auto-grader with point tracking",
          icon = Icons.Default.Quiz,
          iconColor = Color(0xFFD97706),
          onClick = { viewModel.navigateToMoreSub(MoreSubDestination.QUIZZES) }
      )
    }

    item {
      MoreNavTile(
          title = "Official Diploma & Certificate",
          subtitle = "tirOtir AI Academy accreditation certificate generator",
          icon = Icons.Default.WorkspacePremium,
          iconColor = PythonYellow,
          onClick = { viewModel.navigateToMoreSub(MoreSubDestination.CERTIFICATE) }
      )
    }

    item {
      MoreNavTile(
          title = "Settings & Academy Accreditation",
          subtitle = "Institution details, official links & data reset",
          icon = Icons.Default.Settings,
          iconColor = Color(0xFF38BDF8),
          onClick = { viewModel.navigateToMoreSub(MoreSubDestination.SETTINGS) }
      )
    }

    item {
      Spacer(modifier = Modifier.height(70.dp))
    }
  }
}

@Composable
private fun SettingsSubScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var showResetConfirm by remember { mutableStateOf(false) }

  if (showResetConfirm) {
    AlertDialog(
        onDismissRequest = { showResetConfirm = false },
        title = { Text("Reset All Learning Progress?") },
        text = {
          Text("This will reset your completed lessons count, quiz scores, and lab run statistics. This cannot be undone.")
        },
        confirmButton = {
          Button(
              onClick = {
                viewModel.resetAllProgress()
                showResetConfirm = false
              },
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
          ) {
            Text("Reset All")
          }
        },
        dismissButton = {
          TextButton(onClick = { showResetConfirm = false }) {
            Text("Cancel")
          }
        }
    )
  }

  LazyColumn(
      modifier = modifier
          .fillMaxSize()
          .testTag("settings_screen"),
      contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Top Bar
    item {
      Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = { viewModel.navigateToMoreSub(MoreSubDestination.MENU) }) {
          Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "Settings & Institutional Info",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
      }
    }

    // Academy Credential Details
    item {
      Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.School, contentDescription = null, tint = PythonYellow, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text("tirOtir AI Academy", fontWeight = FontWeight.Bold, fontSize = 16.sp)
              Text("Andisheh Pardazan Anzan Institute", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
          }
          Spacer(modifier = Modifier.height(10.dp))
          Text(
              text = "Official vocational and technical training provider accredited under the Technical & Vocational Training Organization (TVTO). Offering 16-week comprehensive Python, AI, and algorithmic thinking curricula.",
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              lineHeight = 17.sp
          )
        }
      }
    }

    // Official Links
    item {
      Text(
          text = "Official Academy Resources",
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }

    item {
      LinkTile(
          title = "Official Website",
          subtitle = "tirotir.ir",
          icon = Icons.Default.Language,
          url = "https://tirotir.ir",
          onOpen = { openUrl(context, "https://tirotir.ir") }
      )
    }

    item {
      LinkTile(
          title = "Curriculum Repository",
          subtitle = "github.com/tirotir-ir/Python-1",
          icon = Icons.Default.Code,
          url = "https://github.com/tirotir-ir/Python-1",
          onOpen = { openUrl(context, "https://github.com/tirotir-ir/Python-1") }
      )
    }

    item {
      LinkTile(
          title = "AI & Philosophy Channel",
          subtitle = "t.me/AI_and_Philosophy",
          icon = Icons.Default.Send,
          url = "https://t.me/AI_and_Philosophy",
          onOpen = { openUrl(context, "https://t.me/AI_and_Philosophy") }
      )
    }

    item {
      LinkTile(
          title = "tirOtir ICDL & Skills Channel",
          subtitle = "t.me/tirotiricdl",
          icon = Icons.Default.Send,
          url = "https://t.me/tirotiricdl",
          onOpen = { openUrl(context, "https://t.me/tirotiricdl") }
      )
    }

    // Data Reset Card
    item {
      Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          border = CardDefaults.outlinedCardBorder()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text("Data & Cache Management", fontWeight = FontWeight.Bold, fontSize = 14.sp)
          Spacer(modifier = Modifier.height(4.dp))
          Text(
              text = "Clear local Room database progress, quizzes, and reset saved session records.",
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.height(10.dp))
          OutlinedButton(
              onClick = { showResetConfirm = true },
              colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFEF4444))
          ) {
            Icon(Icons.Default.DeleteForever, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Reset Learning Progress")
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(80.dp))
    }
  }
}

private fun openUrl(context: android.content.Context, url: String) {
  try {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
  } catch (_: Exception) {}
}

@Composable
private fun MoreNavTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconColor: Color,
    onClick: () -> Unit
) {
  Card(
      modifier = Modifier
          .fillMaxWidth()
          .clickable(onClick = onClick),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      border = CardDefaults.outlinedCardBorder()
  ) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
      Surface(
          shape = RoundedCornerShape(12.dp),
          color = iconColor.copy(alpha = 0.15f),
          modifier = Modifier.size(42.dp)
      ) {
        Box(contentAlignment = Alignment.Center) {
          Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(22.dp))
        }
      }
      Spacer(modifier = Modifier.width(14.dp))
      Column(modifier = Modifier.weight(1f)) {
        Text(text = title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
        Text(text = subtitle, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
      }
      Icon(
          imageVector = Icons.Default.ArrowForward,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
          modifier = Modifier.size(18.dp)
      )
    }
  }
}

@Composable
private fun LinkTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    url: String,
    onOpen: () -> Unit
) {
  Card(
      modifier = Modifier
          .fillMaxWidth()
          .clickable(onClick = onOpen),
      shape = RoundedCornerShape(14.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      border = CardDefaults.outlinedCardBorder()
  ) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
      Surface(
          shape = CircleShape,
          color = PythonBlue.copy(alpha = 0.15f),
          modifier = Modifier.size(36.dp)
      ) {
        Box(contentAlignment = Alignment.Center) {
          Icon(imageVector = icon, contentDescription = null, tint = PythonYellow, modifier = Modifier.size(18.dp))
        }
      }
      Spacer(modifier = Modifier.width(12.dp))
      Column(modifier = Modifier.weight(1f)) {
        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        Text(text = subtitle, fontSize = 11.sp, color = Color(0xFF38BDF8))
      }
      Icon(
          imageVector = Icons.Default.ArrowForward,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
          modifier = Modifier.size(16.dp)
      )
    }
  }
}
