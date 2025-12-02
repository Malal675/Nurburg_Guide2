package com.example.nurburg_guide.ui.features.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nurburg_guide.ui.features.weather.interpretWeatherCode
import com.example.nurburg_guide.ui.theme.AccentGreen
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

@Composable
fun HomeScreen(
    weatherViewModel: HomeWeatherViewModel = viewModel()
) {
    val weatherState = weatherViewModel.uiState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        WeatherHeaderCard(
            uiState = weatherState,
            onRefresh = { weatherViewModel.refresh() }
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Weitere Inhalte (News, Trackstatus, etc.) kommen später",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun WeatherHeaderCard(
    uiState: WeatherUiState,
    onRefresh: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        tonalElevation = 8.dp,
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end   = 16.dp,
                    top   = 0.dp,   // ⬅️ Abstand oben deutlich kleiner
                    bottom = 10.dp  // unten bleibt wie vorher
                ),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Wetter am Ring",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AccentGreen
                )

                IconButton(
                    onClick = onRefresh,
                    enabled = !uiState.isLoading
                ) {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = "Wetter aktualisieren",
                        tint = if (uiState.isLoading) {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        } else {
                            AccentGreen
                        }
                    )
                }
            }

            val lastUpdatedText = uiState.lastUpdated?.let { "Zuletzt aktualisiert: $it" }
                ?: "Noch nicht aktualisiert"

            Text(
                text = lastUpdatedText,
                style = MaterialTheme.typography.labelSmall
            )

            // ⬇️ Rest deines when-Blocks bleibt unverändert
            when {
                uiState.isLoading -> {
                    Text(
                        text = "Lädt Wetterdaten …",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                uiState.errorMessage != null -> {
                    Text(
                        text = uiState.errorMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                else -> {
                    val desc = interpretWeatherCode(uiState.weatherCode)

                    val tempText = uiState.temperatureC?.let { "${it.toInt()}°C" } ?: "–°C"
                    val rainText = uiState.precipitationMm?.let { String.format("%.1f mm", it) } ?: "–"
                    val windText = uiState.windSpeedKmh?.let { String.format("%.1f km/h", it) } ?: "–"

                    Text(
                        text = "${desc.emoji} ${desc.short}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = desc.detail,
                        style = MaterialTheme.typography.bodySmall
                    )

                    Text(
                        text = "Temperatur aktuell: $tempText",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )

                    Text(
                        text = "Regen: $rainText   ·   Wind: $windText",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = "Quelle: Open-Meteo",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
