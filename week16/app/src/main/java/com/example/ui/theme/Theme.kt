package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = MinDarkPrimary,
    secondary = MinDarkSecondary,
    tertiary = MinDarkTertiary,
    background = MinDarkBack,
    surface = MinDarkSurface,
    onPrimary = MinDarkBack,
    onSecondary = MinDarkTextPrimary,
    onBackground = MinDarkTextPrimary,
    onSurface = MinDarkTextSecondary
  )

private val LightColorScheme =
  lightColorScheme(
    primary = MinPrimary,
    secondary = MinSecondary,
    tertiary = MinTertiary,
    background = MinBack,
    surface = MinSurface,
    onPrimary = Color.White,
    onSecondary = MinTextPrimary,
    onBackground = MinTextPrimary,
    onSurface = MinTextSecondary
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Dynamic color is available on Android 12+
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
