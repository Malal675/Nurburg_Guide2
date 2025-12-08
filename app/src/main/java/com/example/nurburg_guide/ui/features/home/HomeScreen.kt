package com.example.nurburg_guide.ui.features.home

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.nurburg_guide.ui.features.weather.interpretWeatherCode
import com.example.nurburg_guide.ui.theme.AccentGreen

/**
 * Explore / Home Screen:
 *  - oben Wetter-Header
 *  - darunter News / Highlights rund um den Ring
 */
@Composable
fun HomeScreen(
    weatherViewModel: HomeWeatherViewModel = viewModel(),
) {
    val weatherState = weatherViewModel.uiState
    val newsItems = remember { sampleRingNews() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        WeatherHeaderCard(
            uiState = weatherState,
            onRefresh = { weatherViewModel.refresh() },
        )

        NewsSection(
            newsItems = newsItems,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

/**
 * Wetterkarte oben (leicht angepasst aus deiner Version).
 */
@Composable
fun WeatherHeaderCard(
    uiState: WeatherUiState,
    onRefresh: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        tonalElevation = 8.dp,
        shape = MaterialTheme.shapes.large,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 0.dp,   // oben flacher
                    bottom = 10.dp,
                ),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Wetter am Ring",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AccentGreen,
                )

                IconButton(
                    onClick = onRefresh,
                    enabled = !uiState.isLoading,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = "Wetter aktualisieren",
                        tint = if (uiState.isLoading) {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        } else {
                            AccentGreen
                        },
                    )
                }
            }

            val lastUpdatedText = uiState.lastUpdated?.let {
                "Zuletzt aktualisiert: $it"
            } ?: "Noch nicht aktualisiert"

            Text(
                text = lastUpdatedText,
                style = MaterialTheme.typography.labelSmall,
            )

            when {
                uiState.isLoading -> {
                    Text(
                        text = "Lädt Wetterdaten …",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }

                uiState.errorMessage != null -> {
                    Text(
                        text = uiState.errorMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                    )
                }

                else -> {
                    val desc = interpretWeatherCode(uiState.weatherCode)
                    val tempText = uiState.temperatureC?.let { "${it.toInt()}°C" } ?: "–°C"
                    val rainText = uiState.precipitationMm?.let {
                        String.format("%.1f mm", it)
                    } ?: "–"
                    val windText = uiState.windSpeedKmh?.let {
                        String.format("%.1f km/h", it)
                    } ?: "–"

                    Text(
                        text = "${desc.emoji} ${desc.short}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = desc.detail,
                        style = MaterialTheme.typography.bodySmall,
                    )
                    Text(
                        text = "Temperatur aktuell: $tempText",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                    )
                    Text(
                        text = "Regen: $rainText · Wind: $windText",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Text(
                        text = "Quelle: Open-Meteo",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

/**
 * Überschrift + Liste der News-Karten.
 */
@Composable
fun NewsSection(
    newsItems: List<RingNewsItem>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = "Ring News & Highlights",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )

        Text(
            text = "Inoffizielle Auswahl – alle Infos ohne Gewähr. Offizielle Details findest du über die verlinkten Quellen.",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(4.dp))

        newsItems.forEach { item ->
            NewsCard(item = item)
        }
    }
}

/**
 * Einzelne News-Karte mit optionalem Bild und Click, der in den Browser führt.
 */
@Composable
fun NewsCard(
    item: RingNewsItem,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }

    val clickableModifier = if (item.articleUrl != null) {
        modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
            ) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.articleUrl))
                context.startActivity(intent)
            }
    } else {
        modifier.fillMaxWidth()
    }

    Surface(
        modifier = clickableModifier,
        tonalElevation = 6.dp,
        shape = MaterialTheme.shapes.large,
    ) {
        Column {
            if (item.imageUrl != null) {
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = item.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentScale = ContentScale.Crop,
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = item.category.label().uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = AccentGreen,
                )

                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    text = item.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    text = "${item.dateLabel} · ${item.source}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
