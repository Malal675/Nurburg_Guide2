package com.example.nurburg_guide.ui.features.calendar.domain

import java.time.LocalDateTime

/**
 * Abstrakte Quelle für Nürburgring-Events.
 */
interface RingCalendarRepository {

    /**
     * Liefert alle Events im Zeitraum [from, to).
     */
    suspend fun getEvents(
        from: LocalDateTime,
        to: LocalDateTime
    ): List<RingEvent>
}
