package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.MainDestination
import com.example.ui.MainViewModel
import com.example.ui.screens.CurriculumScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.IdeScreen
import com.example.ui.screens.MoreScreen
import com.example.ui.screens.ProjectsScreen
import com.example.ui.theme.PythonBlue
import com.example.ui.theme.PythonYellow
import com.example.ui.theme.TirOtirTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      val viewModel: MainViewModel = viewModel()
      val themeMode by viewModel.themeMode.collectAsState()
      val currentDestination by viewModel.currentDestination.collectAsState()

      TirOtirTheme(themeMode = themeMode) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets.safeDrawing,
            bottomBar = {
              TirOtirBottomBar(
                  currentDestination = currentDestination,
                  onNavigate = { viewModel.navigateTo(it) }
              )
            }
        ) { innerPadding ->
          Box(
              modifier = Modifier
                  .fillMaxSize()
                  .padding(innerPadding)
                  .background(MaterialTheme.colorScheme.background)
          ) {
            when (currentDestination) {
              MainDestination.HOME -> HomeScreen(viewModel = viewModel)
              MainDestination.SYLLABUS -> CurriculumScreen(viewModel = viewModel)
              MainDestination.IDE_LAB -> IdeScreen(viewModel = viewModel)
              MainDestination.PROJECTS -> ProjectsScreen(viewModel = viewModel)
              MainDestination.MORE -> MoreScreen(viewModel = viewModel)
            }
          }
        }
      }
    }
  }
}

@Composable
fun TirOtirBottomBar(
    currentDestination: MainDestination,
    onNavigate: (MainDestination) -> Unit
) {
  NavigationBar(
      modifier = Modifier.testTag("main_bottom_nav"),
      containerColor = MaterialTheme.colorScheme.surface,
      contentColor = MaterialTheme.colorScheme.onSurface
  ) {
    val items = listOf(
        Triple(MainDestination.HOME, "Home", Icons.Default.Home),
        Triple(MainDestination.SYLLABUS, "Syllabus", Icons.Default.MenuBook),
        Triple(MainDestination.IDE_LAB, "IDE Lab", Icons.Default.Code),
        Triple(MainDestination.PROJECTS, "Projects", Icons.Default.Widgets),
        Triple(MainDestination.MORE, "More", Icons.Default.MoreHoriz)
    )

    items.forEach { (dest, label, icon) ->
      val isSelected = currentDestination == dest
      NavigationBarItem(
          selected = isSelected,
          onClick = { onNavigate(dest) },
          icon = {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) PythonYellow else MaterialTheme.colorScheme.onSurfaceVariant
            )
          },
          label = {
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) PythonYellow else MaterialTheme.colorScheme.onSurfaceVariant
            )
          },
          colors = NavigationBarItemDefaults.colors(
              indicatorColor = PythonBlue.copy(alpha = 0.25f)
          ),
          modifier = Modifier.testTag("nav_item_${dest.name.lowercase()}")
      )
    }
  }
}
