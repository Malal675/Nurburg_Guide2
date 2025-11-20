// Baustein 4.1: App-Theme mit Dark/Light

package com.example.nurburg_guide.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = RedMain,
    secondary = RedSecondary,
    // weitere Farben später
)

private val LightColorScheme = lightColorScheme(
    primary = RedMain,
    secondary = RedSecondary,
    // weitere Farben später
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
