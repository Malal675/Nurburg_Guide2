package com.example.nurburg_guide.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = AccentGreen,       // Akzent (Buttons, aktive States)
    onPrimary = BlackDark,
    secondary = GreenDark,
    onSecondary = GreyLight,

    background = BlackDark,      // Haupt-Hintergrund (151515)
    onBackground = GreyLight,    // Standard-Text im Dark Mode

    surface = Color(0xFF1E1E1E), // Karten-Hintergrund etwas heller als background
    onSurface = GreyLight,

    outline = GreyMid
)

private val LightColorScheme = lightColorScheme(
    primary = AccentGreen,
    onPrimary = Color.White,
    secondary = GreenDark,
    onSecondary = Color.White,

    background = Color.White,
    onBackground = BlackDark,

    surface = GreyLight,        // Karten-Hintergrund hellgrau
    onSurface = BlackDark,

    outline = GreyMid
)

@Composable
fun NurburgGuideTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
