package com.example.nurburg_guide.ui.features.calendar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.nurburg_guide.ui.features.calendar.domain.InMemoryRingCalendarRepository
import com.example.nurburg_guide.ui.features.calendar.domain.RingEvent
import com.example.nurburg_guide.ui.features.calendar.domain.RingEventCategory
import com.example.nurburg_guide.ui.features.calendar.domain.TrackType
import com.example.nurburg_guide.ui.features.calendar.ui.OfficialCalendarSection
import com.example.nurburg_guide.ui.features.calendar.domain.RingCalendarRepository
import com.example.nurburg_guide.ui.theme.AccentGreen
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.MutableInteractionSource

/**
 * Kalender-Screen:
 * - oben: Wochenleiste (7 Tage) – Auswahl eines Tages
 * - darunter: Events für den ausgewählten Tag
 * - unten: Hinweis + Link zum offiziellen Nürburgring-Kalender
 */
@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
) {
    // Repository einmal pro Composition erzeugen
    val repository: RingCalendarRepository = remember { InMemoryRingCalendarRepository() }

    val today = remember { LocalDate.now() }
    var selectedDate by remember { mutableStateOf(today) }

    // Zeitraum: aktuelle Woche (heute + 6 Tage)
    val eventsState by produceState<List<RingEvent>?>(initialValue = null, repository, today) {
        val from = today.atStartOfDay()
        val to = from.plusDays(7) // exklusiv
        value = repository.getEvents(from, to)
    }

    val eventsByDate = remember(eventsState) {
        eventsState?.groupBy { it.startTime.toLocalDate() } ?: emptyMap()
    }

    val selectedEvents = eventsByDate[selectedDate].orEmpty()
        .sortedBy { it.startTime }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Text(
            text = "Kalender – diese Woche am Ring",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Inoffizieller Überblick über ausgewählte Touristenfahrten & Events " +
                    "für die nächsten 7 Tage. Alle Angaben ohne Gewähr – " +
                    "verbindliche Infos immer im offiziellen Nürburgring-Eventkalender.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Wochenleiste (7 Tage)
        WeekStrip(
            weekStart = today,
            selectedDate = selectedDate,
            onDaySelected = { selectedDate = it },
        )

        Spacer(modifier = Modifier.height(16.dp))

        when {
            eventsState == null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            eventsState!!.isEmpty() -> {
                Text(
                    text = "Für diese Woche sind aktuell keine Events hinterlegt.",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            else -> {
                DayEventsSection(
                    date = selectedDate,
                    events = selectedEvents,
                    modifier = Modifier.weight(1f),
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Die Version aus ui.features.calendar.ui
        OfficialCalendarSection(
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/**
 * Leiste mit 7 Tagen (heute + 6).
 */
@Composable
private fun WeekStrip(
    weekStart: LocalDate,
    selectedDate: LocalDate,
    onDaySelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        for (offset in 0..6) {
            val date = weekStart.plusDays(offset.toLong())
            val isSelected = date == selectedDate
            val isToday = date == LocalDate.now()

            DayChip(
                date = date,
                isSelected = isSelected,
                isToday = isToday,
                onClick = { onDaySelected(date) },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

/**
 * Einzelner Tag als Chip in der Wochenleiste.
 */
@Composable
private fun DayChip(
    date: LocalDate,
    isSelected: Boolean,
    isToday: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
    val dayOfMonth = date.dayOfMonth

    val bgColor =
        if (isSelected) AccentGreen
        else MaterialTheme.colorScheme.surfaceVariant

    val textColor =
        if (isSelected) MaterialTheme.colorScheme.onPrimary
        else MaterialTheme.colorScheme.onSurface

    Surface(
        modifier = modifier
            .height(64.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = LocalIndication.current,
                onClick = onClick
            ),
        shape = MaterialTheme.shapes.medium,
        color = bgColor,
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = dayOfWeek.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = if (isToday) FontWeight.Bold else FontWeight.Medium,
                color = textColor,
                maxLines = 1,
                overflow = TextOverflow.Clip,
            )
            Text(
                text = dayOfMonth.toString(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = textColor,
            )

            if (isToday && !isSelected) {
                Text(
                    text = "HEUTE",
                    style = MaterialTheme.typography.labelSmall,
                    color = textColor.copy(alpha = 0.9f),
                )
            }
        }
    }
}

/**
 * Events für den ausgewählten Tag.
 */
@Composable
private fun DayEventsSection(
    date: LocalDate,
    events: List<RingEvent>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        val dateLabel = date.format(
            DateTimeFormatter.ofPattern("EEEE, dd.MM.yyyy", Locale.getDefault())
        )
        Text(
            text = "Events am $dateLabel",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (events.isEmpty()) {
            Text(
                text = "Für diesen Tag sind aktuell keine Events hinterlegt.",
                style = MaterialTheme.typography.bodyMedium,
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(events) { event ->
                    EventCard(event = event)
                }
            }
        }
    }
}

@Composable
private fun EventCard(
    event: RingEvent,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = formatDate(event.startTime),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = event.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = buildMetaLine(event),
                style = MaterialTheme.typography.bodySmall,
            )

            event.description?.let { desc ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

private fun formatDate(dateTime: LocalDateTime): String {
    val formatter = DateTimeFormatter.ofPattern("EEE, dd.MM.yyyy HH:mm", Locale.getDefault())
    return dateTime.format(formatter)
}

private fun buildMetaLine(event: RingEvent): String {
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())

    val timePart = if (event.endTime != null) {
        val start = event.startTime.format(timeFormatter)
        val end = event.endTime.format(timeFormatter)
        "$start–$end"
    } else {
        event.startTime.format(timeFormatter)
    }

    val track = when (event.track) {
        TrackType.NORDSCHLEIFE -> "Nordschleife"
        TrackType.GP_STRECKE   -> "GP-Strecke"
        TrackType.KOMBINATION  -> "Kombi"
        TrackType.SONSTIGES    -> "Strecke"
    }

    val category = when (event.category) {
        RingEventCategory.TOURISTENFAHRTEN -> "Touristenfahrten"
        RingEventCategory.RENNEN           -> "Rennen"
        RingEventCategory.TRACKDAY         -> "Trackday"
        RingEventCategory.SONSTIGES        -> "Event"
    }

    return "$timePart • $track • $category"
}
