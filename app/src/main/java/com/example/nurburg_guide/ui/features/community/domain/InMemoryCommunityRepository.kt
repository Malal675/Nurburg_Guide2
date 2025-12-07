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
                endTime = day1.withHour(19).withMinute(0),
                category = RingEventCategory.TOURISTENFAHRTEN,
                track = TrackType.NORDSCHLEIFE,
                sourceUrl = OFFICIAL_NUERBURGRING_CALENDAR_URL,
                isHighlight = true
            ),
            RingEvent(
                id = "tf-${day2.toLocalDate()}-day",
                title = "Touristenfahrten (Tag)",
                description = "Tagsüber geöffnet – perfekt für längere Sessions.",
                startTime = day2.withHour(10).withMinute(0),
                endTime = day2.withHour(16).withMinute(0),
                category = RingEventCategory.TOURISTENFAHRTEN,
                track = TrackType.NORDSCHLEIFE,
                sourceUrl = OFFICIAL_NUERBURGRING_CALENDAR_URL,
                isHighlight = true
            ),
            RingEvent(
                id = "special-${day3.toLocalDate()}-big-event",
                title = "Großes Event (z.B. 24h-Feeling)",
                description = "Großes Rennwochenende – volle Hütte, Zeiten ohne Gewähr.",
                startTime = day3.withHour(8).withMinute(0),
                endTime = day3.withHour(22).withMinute(0),
                category = RingEventCategory.RENNEN,
                track = TrackType.KOMBINATION,
                sourceUrl = OFFICIAL_NUERBURGRING_CALENDAR_URL,
                isHighlight = true
            )
        )

        // Nur Events zurückgeben, deren Start in [from, to) liegt
        return demoEvents.filter { event ->
            (event.startTime.isEqual(from) || event.startTime.isAfter(from)) &&
                    event.startTime.isBefore(to)
        }
    }
}
