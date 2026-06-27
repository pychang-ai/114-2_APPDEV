package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = CyberCyan,
    secondary = OceanLightBlue,
    tertiary = StarYellow,
    background = OceanDark,
    surface = OceanCard,
    onBackground = HighContrastWhite,
    onSurface = HighContrastWhite
)

private val LightColorScheme = lightColorScheme(
    primary = OceanCard,
    secondary = OceanLightBlue,
    tertiary = StarYellow,
    background = HighContrastWhite,
    surface = OceanCard,
    onBackground = OceanDark,
    onSurface = HighContrastWhite
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Force dark theme for maritime visual signature
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

