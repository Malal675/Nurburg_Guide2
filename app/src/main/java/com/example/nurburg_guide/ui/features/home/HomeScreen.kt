package com.example.nurburg_guide.ui.features.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.nurburg_guide.R
import com.example.nurburg_guide.ui.features.weather.interpretWeatherCode
import com.example.nurburg_guide.ui.theme.AccentGreen

// zentrale URL für Banner & Dialog
private const val RACE_TAXI_URL =
    "https://www.getspeed-racetaxi.de/?utm_campaign_id=1&utm_adgroupid=&utm_targetid=&utm_loc_interest_ms=1004790&utm_loc_physical_ms=9044649&utm_keyword=&gad_source=1&gad_campaignid=21125883795&gbraid=0AAAAAqcBHeYJLr1YSX8A0gWq_Vfj2Re94&gclid=CjwKCAiA0eTJBhBaEiwA-Pa-hThsLU1Fv46f7rNRCCQaave2D_7AV68ICXrewgGNMUD17Iw_j55siRoCjtwQAvD_BwE"

// ✅ Ticketkauf (offiziell)
private const val TICKETS_URL = "https://www.ghd.nuerburgring.de/"

// ✅ Offizielle Sicherheitsregeln
private const val SAFETY_RULES_URL =
    "https://mobile.nuerburgring.de/driving/touristdrives/safety-regulations?locale=de"

// ✅ Nordschleifen SOS-Line (Dialer)
private const val SOS_LINE_NUMBER = "08000302112"
private const val SOS_LINE_DISPLAY = "0800 0302 112"

// ====== UI Helpers (Outline + Banner-Style) ======

private val BlockShape = RoundedCornerShape(18.dp)

@Composable
private fun OutlinedBlock(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = BlockShape,
            ),
        shape = BlockShape,
        tonalElevation = 4.dp,
    ) {
        content()
    }
}

@Composable
private fun PromoBadge(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(AccentGreen.copy(alpha = 0.18f))
            .border(1.dp, AccentGreen.copy(alpha = 0.55f), RoundedCornerShape(999.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = AccentGreen,
        )
    }
}

/**
 * Explore / Home Screen:
 *  - RaceTaxi-Banner
 *  - Wetter-Header
 *  - Erstfahrer-Guide (Emojis begrenzt)
 *  - SOS / Notfall Card (click-to-dial)
 */
@Composable
fun HomeScreen(
    weatherViewModel: HomeWeatherViewModel = viewModel(),
) {
    val weatherState = weatherViewModel.uiState
    val newsItems = remember { sampleRingNews() }

    var showRaceTaxiDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        // ✅ Banner: mehr “Hero”-Feeling + Badge + Outline
        NlsPromoBanner(
            modifier = Modifier.fillMaxWidth(),
            onClick = { showRaceTaxiDialog = true },
        )

        // ✅ Wetter: eigene Hintergrundfarbe + Outline + mehr Hierarchie
        WeatherHeaderCard(
            uiState = weatherState,
            onRefresh = { weatherViewModel.refresh() },
        )

        // ✅ Guide: jetzt ebenfalls umrandet (über OutlinedBlock)
        FirstTimerInfoCard(
            modifier = Modifier.fillMaxWidth(),
        )

        // ✅ SOS: umrandet
        EmergencyInfoCard(
            modifier = Modifier.fillMaxWidth(),
        )

        // ✅ News: jede Card ist bereits Surface – wir geben der Section einen Rahmen
        if (newsItems.isNotEmpty()) {
            OutlinedBlock(modifier = Modifier.fillMaxWidth()) {
                NewsSection(
                    newsItems = newsItems,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                )
            }
        }
    }

    RaceTaxiInfoDialog(
        open = showRaceTaxiDialog,
        onDismiss = { showRaceTaxiDialog = false },
    )
}

/**
 * Banner oben.
 */
@Composable
fun NlsPromoBanner(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    // ✅ Stärkerer Rahmen + etwas höher + Badge
    Surface(
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
            ) { onClick() }
            .border(
                width = 2.dp,
                color = AccentGreen.copy(alpha = 0.65f),
                shape = BlockShape,
            ),
        shape = BlockShape,
        tonalElevation = 10.dp,
    ) {
        Box {
            Image(
                painter = painterResource(id = R.drawable.home_nls_banner),
                contentDescription = "RaceTaxi – Werde Teil der NLS",
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 170.dp),
                contentScale = ContentScale.Crop,
            )

            // Badge oben links
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp),
            ) {
                PromoBadge("Anzeige · RaceTaxi")
            }

            // kleine “Callout”-Zeile unten
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp),
            ) {
                Text(
                    text = "Tippen für Infos & Buchung",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.78f))
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                )
            }
        }
    }
}

/**
 * RaceTaxi Dialog.
 */
@Composable
fun RaceTaxiInfoDialog(
    open: Boolean,
    onDismiss: () -> Unit,
) {
    if (!open) return
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "RaceTaxi – Werde Teil der NLS",
                style = MaterialTheme.typography.titleLarge,
            )
        },
        text = {
            Text(
                text = "Erlebe die Nordschleife als Beifahrer im GetSpeed RaceTaxi. " +
                        "Über \"Zur Seite\" kommst du direkt zur Buchungsseite.",
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    openUrl(context, RACE_TAXI_URL)
                    onDismiss()
                }
            ) { Text("Zur Seite") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Später") }
        },
    )
}

/**
 * Wetterkarte (ohne Emojis, damit nur deine ausgewählten Emojis vorkommen).
 */
@Composable
fun WeatherHeaderCard(
    uiState: WeatherUiState,
    onRefresh: () -> Unit,
) {
    // ✅ auffälligere “Wetter”-Fläche
    val weatherBg = AccentGreen.copy(alpha = 0.10f)

    OutlinedBlock(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(weatherBg)
                .padding(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = "Wetter am Ring",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = AccentGreen,
                    )
                    val lastUpdatedText = uiState.lastUpdated?.let { "Zuletzt aktualisiert: $it" }
                        ?: "Noch nicht aktualisiert"

                    Text(
                        text = lastUpdatedText,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

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

            when {
                uiState.isLoading -> Text("Lädt Wetterdaten …", style = MaterialTheme.typography.bodyMedium)

                uiState.errorMessage != null -> Text(
                    text = uiState.errorMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                )

                else -> {
                    val desc = interpretWeatherCode(uiState.weatherCode)
                    val tempText = uiState.temperatureC?.let { "${it.toInt()}°C" } ?: "–°C"
                    val rainText = uiState.precipitationMm?.let { String.format("%.1f mm", it) } ?: "–"
                    val windText = uiState.windSpeedKmh?.let { String.format("%.1f km/h", it) } ?: "–"

                    // ✅ “Hauptwert” größer + Details klar getrennt
                    Text(
                        text = desc.short,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(desc.detail, style = MaterialTheme.typography.bodySmall)

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Temperatur: $tempText",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
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
 * ✅ Guide-Card (nur diese Emojis):
 * 🚦 🟡 🔴 🧰 ⛽ 💡
 */
@Composable
fun FirstTimerInfoCard(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    OutlinedBlock(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = "Erstfahrer Guide",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = AccentGreen,
            )

            Text(
                text = "Kurz & praxisnah (ohne Gewähr). Vor Ort gelten Schilder, Flaggen und Anweisungen.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "🚦 Regeln & Verhalten",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
            )
            Bullet("• Rechts fahren, Spiegel nutzen, schnellere links vorbei lassen.")
            Bullet("• StVO gilt – Rechtsüberholen ist verboten.")
            Bullet("🟡 Gelb: Tempo deutlich runter, nicht überholen, jederzeit Gefahr/Trümmer/Stau möglich.")
            Bullet("🔴 Rot: Strecke gesperrt bis wieder geöffnet – keine Fahrten möglich.")
            Bullet("• Foto-/Film-/Videoaufnahmen während Touristenfahrten sind grundsätzlich verboten.")
            Bullet("• Baustellen/Tempolimits strikt einhalten – grundsätzlich angepasste Geschwindigkeit.")

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "🧰 Auto Setup",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
            )
            Bullet("• Bei Öl/Kühlwasser/Kraftstoff-Verlust: nicht weiterfahren.")
            Bullet("• Verschmutzung/Leck sofort der Streckensicherung melden.")
            Bullet("• Fahrerlaubnis + Fahrzeugschein mitführen.")
            Bullet("• Innenraum: alles Lose raus (kein Plunder im Auto).")
            Bullet("⛽ Tank: nicht auf Reserve starten – lieber früh tanken als spät suchen.")

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Quick Tipps",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
            )
            Bullet("• Erste Runde ruhig: Reifen & Bremsen anwärmen, Rhythmus finden.")
            Bullet("💡 Quick Tipp: Lieber wenige saubere Runden als „Bestzeit“ – weniger Stress & Risiko.")

            Spacer(modifier = Modifier.height(8.dp))

            // ✅ FIX: kein Umbruch mitten im Wort + sauber zentriert + etwas weniger Padding
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                OutlinedButton(
                    onClick = { openGeoQuery(context, "Tankstelle Nürburgring") },
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 44.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 12.dp),
                ) { ButtonLabel("⛽ Tankstellen") }

                Button(
                    onClick = { openUrl(context, TICKETS_URL) },
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 44.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 12.dp),
                ) { ButtonLabel("Ticketverkauf") }

                OutlinedButton(
                    onClick = { openGeoQuery(context, "Nürburgring Touristenfahrten Zufahrt") },
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 44.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 12.dp),
                ) { ButtonLabel("Zufahrt") }
            }
        }
    }
}

/**
 * 📞 SOS / Notfall – click-to-dial + offizieller Link + Disclaimer.
 */
@Composable
fun EmergencyInfoCard(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    OutlinedBlock(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = "📞 SOS / Notfall (Touristenfahrten)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = AccentGreen,
            )

            Text(
                text = "Hinweis: Diese App ist inoffiziell und steht in keiner Verbindung zum Nürburgring " +
                        "oder seinen Partnern. Alle Angaben ohne Gewähr – maßgeblich sind offizielle Aushänge, Ansagen und die Website.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Text(
                text = "Ruf nur an, wenn du sicher stehst (z. B. abseits der Fahrbahn / hinter Leitplanke). " +
                        "Nicht während der Fahrt telefonieren.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Text(
                text =
                    "• Bei Unfall, Panne oder Verschmutzung melden.\n" +
                            "• Bei Öl/Kühlwasser/Kraftstoff-Verlust: nicht weiterfahren.\n" +
                            "• Am Telefon kurz: WO (Abschnitt), WAS (Unfall/Panne/Öl), WIE VIELE.\n" +
                            "• Bei akuter Lebensgefahr immer 112.",
                style = MaterialTheme.typography.bodyMedium,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Button(
                    onClick = { dialNumber(context, SOS_LINE_NUMBER) },
                    modifier = Modifier.weight(1f),
                ) { Text("📞 SOS-Line $SOS_LINE_DISPLAY") }

                OutlinedButton(
                    onClick = { dialNumber(context, "112") },
                    modifier = Modifier.weight(1f),
                ) { Text("112 Notruf") }
            }

            TextButton(
                onClick = { openUrl(context, SAFETY_RULES_URL) },
            ) {
                Text("Offizielle Sicherheitsregeln öffnen")
            }
        }
    }
}

@Composable
private fun Bullet(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
    )
}

/**
 * ✅ Helper: verhindert Wort-Umbruch mitten im Wort in Buttons (Tankstellen/Ticketkauf etc.)
 */
@Composable
private fun ButtonLabel(text: String) {
    Text(
        text = text,
        maxLines = 1,
        softWrap = false,
        overflow = TextOverflow.Clip,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.labelMedium,
        modifier = Modifier.fillMaxWidth(),
    )
}

private fun openGeoQuery(
    context: Context,
    query: String,
) {
    val uri = Uri.parse("geo:0,0?q=${Uri.encode(query)}")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    context.startActivity(intent)
}

private fun openUrl(
    context: Context,
    url: String,
) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}

/**
 * ACTION_DIAL = öffnet Dialer mit Nummer (keine Permission nötig).
 */
private fun dialNumber(
    context: Context,
    number: String,
) {
    val intent = Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("tel:$number")
    }
    context.startActivity(intent)
}

/**
 * Optional: News
 */
@Composable
fun NewsSection(
    newsItems: List<RingNewsItem>,
    modifier: Modifier = Modifier,
) {
    if (newsItems.isEmpty()) return

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
        modifier = clickableModifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = BlockShape,
            ),
        tonalElevation = 4.dp,
        shape = BlockShape,
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
