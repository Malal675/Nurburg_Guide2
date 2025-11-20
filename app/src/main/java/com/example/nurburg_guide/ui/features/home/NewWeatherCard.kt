package com.example.nurburg_guide.ui.features.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nurburg_guide.ui.features.weather.interpretWeatherCode

/**
 * NEUE Wetterkarte für den Home-Screen (V2).
 *
 * Wir lassen die alte Karte erstmal in Ruhe und zeigen diese zusätzlich,
 * damit du den Unterschied direkt siehst.
 */
@Composable
fun NewWeatherCard(
    uiState: WeatherUiState
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        tonalElevation = 12.dp,
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Titel fett, klar sichtbar
            Text(
                text = "Wetter am Ring (neu)",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

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

                    // Obere Zeile: Emoji + Beschreibung links, Temperatur rechts
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = desc.emoji,
                                style = MaterialTheme.typography.headlineMedium
                            )
                            Column(
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Text(
                                    text = desc.short,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = desc.detail,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }

                        Text(
                            text = tempText,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Untere Zeile: Niederschlag & Wind
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Niederschlag",
                                style = MaterialTheme.typography.labelMedium
                            )
                            Text(
                                text = rainText,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Wind",
                                style = MaterialTheme.typography.labelMedium
                            )
                            Text(
                                text = windText,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Quelle: Open-Meteo (neu)",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
