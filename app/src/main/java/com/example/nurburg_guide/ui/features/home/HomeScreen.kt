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

@Composable
fun HomeScreen(
    weatherViewModel: HomeWeatherViewModel = viewModel()
) {
    val weatherState = weatherViewModel.uiState

    Column(
        modifier = Modifier
            .fillMaxSize()
            // HIER: mehr Abstand nach oben, damit die Karte mittiger sitzt
            .padding(start = 16.dp, end = 16.dp, top = 56.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
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

/**
 * Wetterkarte mit:
 *  - Titel "Wetter am Ring"
 *  - Zeile: Zuletzt aktualisiert + "Aktualisieren"
 *  - Statuszeile mit Emoji
 *  - Detailtext
 *  - Temperaturzeile
 *  - Regen / Wind
 *  - Quelle
 */
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Titel
            Text(
                text = "Wetter am Ring",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            // Zeile: Zuletzt aktualisiert + Refresh
            val lastUpdatedText = uiState.lastUpdated?.let { "Zuletzt aktualisiert: $it" }
                ?: "Noch nicht aktualisiert"

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = lastUpdatedText,
                    style = MaterialTheme.typography.labelSmall
                )

                val interactionSource = remember { MutableInteractionSource() }

                Text(
                    text = if (uiState.isLoading) "Aktualisiere…" else "Aktualisieren",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable(
                        enabled = !uiState.isLoading,
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        onRefresh()
                    }
                )
            }

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

                    // Statuszeile mit Emoji
                    Text(
                        text = "${desc.emoji} ${desc.short}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold
                    )

                    // Detailbeschreibung
                    Text(
                        text = desc.detail,
                        style = MaterialTheme.typography.bodySmall
                    )

                    // Temperatur
                    Text(
                        text = "Temperatur aktuell: $tempText",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )

                    // Regen / Wind
                    Text(
                        text = "Regen: $rainText   ·   Wind: $windText",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    // Quelle
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
