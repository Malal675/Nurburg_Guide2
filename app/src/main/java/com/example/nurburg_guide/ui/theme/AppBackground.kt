package com.example.nurburg_guide.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.nurburg_guide.R

@Composable
fun AppBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    // Scrim automatisch passend zum Theme (Dark/Light)
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val scrim = if (isDark) {
        Color.Black.copy(alpha = 0.50f)   // Dark Theme: Bild sichtbar, aber Text bleibt gut
    } else {
        Color.White.copy(alpha = 0.65f)   // Light Theme: Bild ruhig halten
    }

    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg_ring),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // „Durchschimmern“ aber lesbar
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(scrim)
        )

        content()
    }
}
