package com.example.nurburg_guide.ui.features.calendar.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.nurburg_guide.ui.features.calendar.domain.OFFICIAL_NUERBURGRING_CALENDAR_URL

/**
 * Hinweis + Button zum offiziellen Nürburgring-Kalender.
 *
 * Rechtlich wichtig:
 * - Klarer Hinweis: App ist nicht offiziell.
 * - Alle Angaben ohne Gewähr.
 * - Button öffnet den offiziellen Kalender im Browser.
 */
@Composable
fun OfficialCalendarSection(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(modifier = modifier) {
        Text(
            text = "Hinweis: Dies ist nicht die offizielle App des Nürburgrings. " +
                    "Alle Terminangaben in der App sind ohne Gewähr. " +
                    "Verbindliche Informationen findest du nur im offiziellen Nürburgring-Kalender."
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(OFFICIAL_NUERBURGRING_CALENDAR_URL)
                )
                context.startActivity(intent)
            }
        ) {
            Text("Offiziellen Nürburgring-Kalender öffnen")
        }
    }
}
