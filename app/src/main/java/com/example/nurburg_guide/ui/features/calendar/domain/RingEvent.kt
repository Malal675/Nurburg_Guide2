package com.example.nurburg_guide.ui.features.calendar.domain

import java.time.LocalDateTime

/**
 * Domain-Modell für Events am Nürburgring, die wir im Kalender / "Next 3 Days"-Block anzeigen.
 *
 * WICHTIG (rechtlich):
 * - Dies ist NICHT die offizielle App des Nürburgrings.
 * - Zeiten und Inhalte sind ohne Gewähr.
 * - Für verbindliche Infos immer die offizielle Nürburgring-Seite / den offiziellen Kalender nutzen.
 */
data class RingEvent(
    val id: String,                    // z.B. "tf-2025-04-18-evening"
    val title: String,                 // z.B. "Touristenfahrten Abend"
    val description: String?,          // eigener Text, nicht 1:1 von der Ring-Seite kopiert
    val startTime: LocalDateTime,
    val endTime: LocalDateTime?,
    val category: RingEventCategory,
    val track: TrackType,
    val sourceUrl: String? = null,     // z.B. Link zur offiziellen Event-/Kalender-Seite
    val isHighlight: Boolean = false   // z.B. für Explore-Feed oder "besonders wichtig"
)

enum class RingEventCategory {
    TOURISTENFAHRTEN,
    RENNEN,
    TRACKDAY,
    SONSTIGES
}

enum class TrackType {
    NORDSCHLEIFE,
    GP_STRECKE,
    KOMBINATION,
    SONSTIGES
}

/**
 * Link zur offiziellen Nürburgring-Kalenderübersicht.
 */
const val OFFICIAL_NUERBURGRING_CALENDAR_URL: String =
    "https://nuerburgring.de/events?locale=en"
