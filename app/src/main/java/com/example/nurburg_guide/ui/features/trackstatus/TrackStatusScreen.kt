package com.example.nurburg_guide.ui.features.trackstatus

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nurburg_guide.data.trackstatus.NORDSCHLEIFE_SECTIONS
import com.example.nurburg_guide.data.trackstatus.SectorState
import com.example.nurburg_guide.data.trackstatus.SectorStatus
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.delay

@Composable
fun TrackStatusScreen(
    viewModel: TrackStatusViewModel = viewModel()
) {
    // 1..32 Sektoren
    val sectorStates by viewModel.sectors.collectAsState()

    // globaler UI-State (Rot-Banner, Loading, ...)
    val state by viewModel.uiState.collectAsState()

    // 🔁 Globaler 1-Sekunden-Ticker für Countdown + Ablauf der Timer
    var now by remember { mutableStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while (true) {
            now = System.currentTimeMillis()
            viewModel.cleanupExpiredStates()
            delay(1_000L)
        }
    }

    // ---------- Location + Permission ----------
    val context = LocalContext.current
    val fusedClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    var hasLocationPermission by remember { mutableStateOf(false) }
    var userLocation by remember { mutableStateOf<Location?>(null) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasLocationPermission = granted
        if (granted) {
            fusedClient.lastLocation.addOnSuccessListener { loc ->
                if (loc != null) {
                    userLocation = loc
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        val granted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (granted) {
            hasLocationPermission = true
            fusedClient.lastLocation.addOnSuccessListener { loc ->
                if (loc != null) {
                    userLocation = loc
                }
            }
        } else {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    val canReportFromLocation: Boolean = remember(userLocation) {
        userLocation?.let { loc ->
            isNearNordschleife(loc.latitude, loc.longitude)
        } ?: false
    }

    var showRedDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Text(
            text = "Track Status",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 🔴 Banner, wenn Strecke auf Rot steht
        if (state.isTrackRed) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    text = "Strecke aktuell ROT (vermutlich gesperrt oder Unfall).",
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onError,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        // 🔴 Button für Rot melden / Rot aufheben
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { showRedDialog = true },
                enabled = hasLocationPermission && canReportFromLocation,
            ) {
                Text(
                    text = if (state.isTrackRed) "Rot aufheben" else "Strecke ROT melden"
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            if (!hasLocationPermission) {
                Text(
                    text = "Standort nötig für Meldungen.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            } else if (!canReportFromLocation) {
                Text(
                    text = "Nur vor Ort meldbar.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Lade Trackstatus …")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(sectorStates) { sectorState ->
                    TrackSectionCard(
                        section = sectorState,
                        canReportFromLocation = canReportFromLocation && hasLocationPermission,
                        onReportYellow = { viewModel.reportYellow(sectorState.id) },
                        currentTimeMillis = now
                    )
                }
            }
        }
    }

    // 🔴 Bestätigungsdialog für Rot
    if (showRedDialog) {
        AlertDialog(
            onDismissRequest = { showRedDialog = false },
            title = {
                Text(
                    text = if (state.isTrackRed) "Rot aufheben?" else "Strecke ROT melden?",
                    fontWeight = FontWeight.SemiBold
                )
            },
            text = {
                Text(
                    text = if (state.isTrackRed) {
                        "Bist du sicher, dass du den Rot-Status aufheben möchtest?"
                    } else {
                        "Nur melden, wenn die Strecke wirklich gesperrt ist oder ein schwerer Vorfall vorliegt."
                    }
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showRedDialog = false
                        viewModel.setTrackRed(!state.isTrackRed)
                    }
                ) {
                    Text("Bestätigen")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showRedDialog = false }
                ) {
                    Text("Abbrechen")
                }
            }
        )
    }
}

@Composable
private fun TrackSectionCard(
    section: SectorState,
    canReportFromLocation: Boolean,
    onReportYellow: () -> Unit,
    currentTimeMillis: Long
) {
    // passenden Anzeigenamen über ID aus den Definitionen holen
    val displayName = run {
        val index = section.id - 1
        if (index in NORDSCHLEIFE_SECTIONS.indices) {
            NORDSCHLEIFE_SECTIONS[index].displayName
        } else {
            "Sektor ${section.id}"
        }
    }

    // Farben abhängig vom SectorStatus
    val (statusText, statusColor, chipColor) = when (section.status) {
        SectorStatus.GREEN -> Triple("Grün", Color(0xFF2E7D32), Color(0x332E7D32))
        SectorStatus.YELLOW -> Triple("Gelb", Color(0xFFF9A825), Color(0x33F9A825))
        SectorStatus.RED -> Triple("Rot", Color(0xFFC62828), Color(0x33C62828))
    }

    // ⏱️ Restzeit in Sekunden, wenn Gelb aktiv ist
    val remainingSeconds: Int? =
        if (section.status == SectorStatus.YELLOW && section.yellowUntilMillis != null) {
            val diffMillis = section.yellowUntilMillis - currentTimeMillis
            val secs = (diffMillis / 1000L).toInt()
            if (secs > 0) secs else 0
        } else {
            null
        }

    val timerText: String? = remainingSeconds?.let { secs ->
        val m = secs / 60
        val s = secs % 60
        String.format("%02d:%02d", m, s)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = displayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )

                // Farbige Status-Chip-Zeile
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatusDot(color = statusColor)
                    Box(
                        modifier = Modifier
                            .background(
                                color = chipColor,
                                shape = MaterialTheme.shapes.small
                            )
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = statusText,
                            style = MaterialTheme.typography.bodySmall,
                            color = statusColor,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                if (!canReportFromLocation) {
                    Text(
                        text = "Nur vor Ort meldbar",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Rechts: entweder Gelb-Button ODER Timer-Badge
            if (section.status != SectorStatus.YELLOW) {
                // Schlanker, zentrierter Gelb-Button
                Button(
                    onClick = onReportYellow,
                    enabled = canReportFromLocation,
                    modifier = Modifier
                        .height(36.dp)               // etwas dünner
                        .widthIn(min = 80.dp),       // nicht zu breit, aber Platz für Text
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text(
                        text = "Gelb",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                // Gelb aktiv: grau hinterlegter Timer an Stelle des Buttons
                Box(
                    modifier = Modifier
                        .height(36.dp)
                        .widthIn(min = 80.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(18.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = timerText ?: "00:00",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusDot(color: Color) {
    Box(
        modifier = Modifier
            .size(10.dp)
            .background(color = color, shape = MaterialTheme.shapes.small)
    )
}

/**
 * Grober Check: ist der User in der Nähe der Nordschleife?
 */
private fun isNearNordschleife(userLat: Double, userLon: Double): Boolean {
    val ringLat = 50.3460365
    val ringLon = 6.9652723

    val results = FloatArray(1)
    Location.distanceBetween(userLat, userLon, ringLat, ringLon, results)
    val distanceMeters = results[0]

    return distanceMeters <= 8_000f
}