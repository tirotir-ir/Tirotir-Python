package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

enum class AppThemeMode {
  SYSTEM,
  LIGHT,
  DARK,
  SEPIA
}

private val DarkColorScheme =
    darkColorScheme(
        primary = PythonYellow,
        onPrimary = Color(0xFF1E293B),
        primaryContainer = PythonBlueDark,
        onPrimaryContainer = PythonYellow,
        secondary = PythonBlueLight,
        onSecondary = Color.White,
        secondaryContainer = Color(0xFF1E293B),
        onSecondaryContainer = Color(0xFFE2E8F0),
        tertiary = Color(0xFF38BDF8),
        onTertiary = Color(0xFF0F172A),
        background = DarkBg,
        onBackground = DarkTextPrimary,
        surface = DarkSurface,
        onSurface = DarkTextPrimary,
        surfaceVariant = DarkSurfaceElevated,
        onSurfaceVariant = DarkTextSecondary,
        outline = Color(0xFF475569),
        error = Color(0xFFEF4444),
        onError = Color.White,
    )

private val LightColorScheme =
    lightColorScheme(
        primary = PythonBlue,
        onPrimary = Color.White,
        primaryContainer = Color(0xFFDBEAFE),
        onPrimaryContainer = Color(0xFF1E3A8A),
        secondary = Color(0xFFD97706),
        onSecondary = Color.White,
        secondaryContainer = Color(0xFFFEF3C7),
        onSecondaryContainer = Color(0xFF92400E),
        tertiary = Color(0xFF0284C7),
        onTertiary = Color.White,
        background = LightBg,
        onBackground = LightTextPrimary,
        surface = LightSurface,
        onSurface = LightTextPrimary,
        surfaceVariant = LightSurfaceElevated,
        onSurfaceVariant = LightTextSecondary,
        outline = Color(0xFFCBD5E1),
        error = Color(0xFFDC2626),
        onError = Color.White,
    )

private val SepiaColorScheme =
    lightColorScheme(
        primary = SepiaPrimary,
        onPrimary = Color(0xFFFAF2E1),
        primaryContainer = SepiaSurfaceElevated,
        onPrimaryContainer = SepiaTextPrimary,
        secondary = SepiaSecondary,
        onSecondary = Color(0xFFFAF2E1),
        secondaryContainer = Color(0xFFE2D0AF),
        onSecondaryContainer = SepiaTextPrimary,
        tertiary = Color(0xFF9A3412),
        onTertiary = Color(0xFFFAF2E1),
        background = SepiaBg,
        onBackground = SepiaTextPrimary,
        surface = SepiaSurface,
        onSurface = SepiaTextPrimary,
        surfaceVariant = SepiaSurfaceElevated,
        onSurfaceVariant = SepiaTextSecondary,
        outline = Color(0xFFC7B38C),
        error = Color(0xFFB91C1C),
        onError = Color.White,
    )

@Composable
fun TirOtirTheme(
    themeMode: AppThemeMode = AppThemeMode.DARK,
    content: @Composable () -> Unit
) {
  val systemIsDark = isSystemInDarkTheme()
  val colorScheme: ColorScheme =
      when (themeMode) {
        AppThemeMode.DARK -> DarkColorScheme
        AppThemeMode.LIGHT -> LightColorScheme
        AppThemeMode.SEPIA -> SepiaColorScheme
        AppThemeMode.SYSTEM -> if (systemIsDark) DarkColorScheme else LightColorScheme
      }

  MaterialTheme(
      colorScheme = colorScheme,
      typography = Typography,
      content = content
  )
}
