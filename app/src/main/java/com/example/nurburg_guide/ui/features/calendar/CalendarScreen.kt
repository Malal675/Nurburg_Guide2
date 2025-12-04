package com.example.nurburg_guide.ui.features.calendar.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.nurburg_guide.ui.features.calendar.domain.InMemoryRingCalendarRepository
import com.example.nurburg_guide.ui.features.calendar.domain.RingCalendarRepository
import com.example.nurburg_guide.ui.features.calendar.domain.RingEvent
import com.example.nurburg_guide.ui.features.calendar.domain.RingEventCategory
import com.example.nurburg_guide.ui.features.calendar.domain.TrackType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Kalender-Screen:
 * Zeigt Events für die nächsten 3 Tage + Hinweis/Link zum offiziellen Kalender.
 */
@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    repository: RingCalendarRepository = remember { InMemoryRingCalendarRepository() }
) {
    // Zeitraum: jetzt bis in 3 Tagen
    val now = remember { LocalDateTime.now() }
    val to = remember { now.plusDays(3) }

    val eventsState by produceState<List<RingEvent>?>(initialValue = null, repository, now, to) {
        value = repository.getEvents(now, to)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "In den nächsten 3 Tagen am Ring",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Touristenfahrten und Events in den nächsten 3 Tagen – ohne Gewähr. " +
                    "Verbindliche Zeiten und kurzfristige Änderungen findest du im offiziellen Nürburgring-Kalender.",
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        when {
            eventsState == null -> {
                CircularProgressIndicator()
            }

            eventsState!!.isEmpty() -> {
                Text(
                    text = "Für diesen Zeitraum sind aktuell keine Events hinterlegt.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    items(eventsState!!) { event ->
                        EventCard(event = event)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hinweis + offizieller Kalender-Link
        OfficialCalendarSection()
    }
}

@Composable
private fun EventCard(
    event: RingEvent,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = formatDate(event.startTime),
                style = MaterialTheme.typography.labelSmall
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = event.title,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = buildMetaLine(event),
                style = MaterialTheme.typography.bodySmall
            )

            event.description?.let { desc ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

private fun formatDate(dateTime: LocalDateTime): String {
    val formatter = DateTimeFormatter.ofPattern("EEE, dd.MM.yyyy HH:mm")
    return dateTime.format(formatter)
}

private fun buildMetaLine(event: RingEvent): String {
    val timePart = if (event.endTime != null) {
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        val start = event.startTime.format(timeFormatter)
        val end = event.endTime.format(timeFormatter)
        "$start–$end"
    } else {
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        event.startTime.format(timeFormatter)
    }

    val track = when (event.track) {
        TrackType.NORDSCHLEIFE -> "Nordschleife"
        TrackType.GP_STRECKE -> "GP-Strecke"
        TrackType.KOMBINATION -> "Kombi"
        TrackType.SONSTIGES -> "Strecke"
    }

    val category = when (event.category) {
        RingEventCategory.TOURISTENFAHRTEN -> "Touristenfahrten"
        RingEventCategory.RENNEN -> "Rennen"
        RingEventCategory.TRACKDAY -> "Trackday"
        RingEventCategory.SONSTIGES -> "Event"
    }

    return "$timePart • $track • $category"
}
