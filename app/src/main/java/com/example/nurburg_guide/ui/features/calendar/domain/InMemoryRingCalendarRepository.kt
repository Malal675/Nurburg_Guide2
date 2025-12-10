package com.example.nurburg_guide.ui.features.calendar.domain

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

/**
 * Einfache InMemory-Implementation für Tests / Entwicklung.
 *
 * Erzeugt Demo-Events relativ zu [from]:
 * - Heute Abend: Touristenfahrten
 * - Morgen: Touristenfahrten tagsüber
 * - Übermorgen: Special Event
 */
class InMemoryRingCalendarRepository : RingCalendarRepository {

    override suspend fun getEvents(
        from: LocalDateTime,
        to: LocalDateTime
    ): List<RingEvent> {
        // Auf Tagesanfang runden
        val baseDayStart = from.truncatedTo(ChronoUnit.DAYS)
        val day1 = baseDayStart
        val day2 = baseDayStart.plusDays(1)
        val day3 = baseDayStart.plusDays(2)

        val demoEvents = listOf(
            RingEvent(
                id = "tf-${day1.toLocalDate()}-evening",
                title = "Touristenfahrten (Abend)",
                description = "After-Work-Laps. Zeiten ohne Gewähr.",
                startTime = day1.withHour(17).withMinute(0),
                endTime = day1.withHour(19).withMinute(30),
                category = RingEventCategory.TOURISTENFAHRTEN,
                track = TrackType.NORDSCHLEIFE,
                isHighlight = true
            ),
            RingEvent(
                id = "tf-${day2.toLocalDate()}-day",
                title = "Touristenfahrten (Tag)",
                description = "Tagsüber offen für TF.",
                startTime = day2.withHour(9).withMinute(0),
                endTime = day2.withHour(18).withMinute(0),
                category = RingEventCategory.TOURISTENFAHRTEN,
                track = TrackType.KOMBINATION
            ),
            RingEvent(
                id = "race-${day3.toLocalDate()}",
                title = "VLN / NLS-Lauf",
                description = "Langstreckenrennen – live am Ring.",
                startTime = day3.withHour(12).withMinute(0),
                endTime = day3.withHour(18).withMinute(0),
                category = RingEventCategory.RENNEN,
                track = TrackType.NORDSCHLEIFE,
                isHighlight = true
            )
        )

        // Nur Events im gewünschten Zeitraum zurückgeben
        return demoEvents.filter { event ->
            val end = event.endTime ?: event.startTime
            event.startTime >= from && end <= to
        }
    }
}
