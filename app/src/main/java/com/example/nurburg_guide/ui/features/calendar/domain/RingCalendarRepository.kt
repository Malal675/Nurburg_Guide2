package com.example.nurburg_guide.ui.features.calendar.domain

import java.time.LocalDateTime

interface RingCalendarRepository {
    suspend fun getEvents(
        from: LocalDateTime,
        to: LocalDateTime
    ): List<RingEvent>
}
