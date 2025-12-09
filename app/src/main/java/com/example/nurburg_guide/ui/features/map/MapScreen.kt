package com.example.nurburg_guide.ui.features.map

// Android / System
import android.annotation.SuppressLint
import android.location.Location

// Compose Foundation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape

// Icons / Material3
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

// Compose Runtime / UI
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

// Google Play Services (Fused Location Provider)
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

// Google Maps (classic)
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng

// Google Maps Compose
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.example.nurburg_guide.ui.features.map.TrackSectorsOverlay
import com.example.nurburg_guide.ui.features.trackstatus.SectorState
import com.example.nurburg_guide.ui.features.trackstatus.SectorStatus

// Maplocations
import com.example.nurburg_guide.ui.features.map.data.AllLocations
import com.example.nurburg_guide.ui.features.map.model.GuideLocation
import com.example.nurburg_guide.ui.features.map.model.LocationCategory

// Sektoren

import com.example.nurburg_guide.ui.features.map.sectors.TiergartenSector

// Coroutines
import kotlinx.coroutines.launch

@SuppressLint("MissingPermission")
@Composable
fun MapScreen() {
    val context = LocalContext.current
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    var userLocation by remember { mutableStateOf<LatLng?>(null) }

    // 🔁 Startposition wieder wie früher: Nürburgring allgemein
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(50.36499, 6.97163),
            12.5f
        )
    }

    var selectedLocation by remember { mutableStateOf<GuideLocation?>(null) }
    val coroutineScope = rememberCoroutineScope()

    // 👉 Nur zum Testen: Tiergarten (ID 1) ist GELB
    // später: kommt das aus TrackStatusViewModel
    val sectorsState: List<SectorState> = remember {
        listOf(
            SectorState(
                sectorId = 1,
                status = SectorStatus.GREEN
            )
        )
    }

    // ---------- FILTER-STATE ----------
    var filterPanelOpen by remember { mutableStateOf(false) }

    var selectedCategories by remember {
        mutableStateOf(LocationCategory.values().toSet())
    }

    val locationsToShow = AllLocations.all.filter { it.category in selectedCategories }

    // ---------- Location-Request ----------
    val locationRequest = remember {
        LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            5_000L
        )
            .setMinUpdateIntervalMillis(2_000L)
            .build()
    }

    val locationListener = remember {
        object : LocationListener {
            override fun onLocationChanged(location: Location) {
                userLocation = LatLng(location.latitude, location.longitude)
            }
        }
    }

    LaunchedEffect(Unit) {
        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationListener,
                null
            )

            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    userLocation = LatLng(location.latitude, location.longitude)
                }
            }
        } catch (_: SecurityException) {
            // Map läuft trotzdem, nur ohne MyLocation
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // MAP
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(
                myLocationButtonEnabled = false
            )
        ) {
            // ✅ Sektoren-Overlay (alle Polylines + Farben)
            TrackSectorsOverlay(sectorsState = sectorsState)

            // Marker für Locations
            locationsToShow.forEach { location ->
                Marker(
                    state = MarkerState(
                        position = LatLng(location.latitude, location.longitude)
                    ),
                    title = location.name,
                    onClick = {
                        selectedLocation = location
                        false
                    }
                )
            }
        }

        // RECHTE SEITE: Panel + Pfeil in einer Row
        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (filterPanelOpen) {
                Surface(
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        bottomStart = 16.dp
                    ),
                    tonalElevation = 4.dp,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Column(
                        modifier = Modifier
                            .width(220.dp)
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "Filter",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        LocationCategory.values().forEach { category ->
                            val checked = category in selectedCategories

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Checkbox(
                                    checked = checked,
                                    onCheckedChange = {
                                        selectedCategories =
                                            toggleCategory(selectedCategories, category)
                                    }
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = category.displayName(),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // "Alle ein/aus" tappbar ohne clickable()
                        Box(
                            modifier = Modifier
                                .padding(vertical = 4.dp)
                                .pointerInput(Unit) {
                                    detectTapGestures {
                                        selectedCategories =
                                            if (selectedCategories.size == LocationCategory.values().size)
                                                emptySet()
                                            else
                                                LocationCategory.values().toSet()
                                    }
                                }
                        ) {
                            Text(
                                text = "Alle ein/ausblenden",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))
            }

            // Pfeil-FAB immer rechts außen
            FloatingActionButton(
                onClick = { filterPanelOpen = !filterPanelOpen },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = if (filterPanelOpen) Icons.Filled.ChevronRight else Icons.Filled.ChevronLeft,
                    contentDescription = if (filterPanelOpen) "Filter schließen" else "Filter öffnen"
                )
            }
        }

        // MyLocation-Button unten rechts
        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .offset(x = (-40).dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FloatingActionButton(
                onClick = {
                    userLocation?.let { here ->
                        coroutineScope.launch {
                            cameraPositionState.animate(
                                CameraUpdateFactory.newLatLngZoom(here, 16f)
                            )
                        }
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 12.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.MyLocation,
                    contentDescription = "Zu mir zentrieren"
                )
            }
        }

        // TODO: selectedLocation-Card später
    }
}

// -------- Hilfsfunktionen für Filter --------

private fun toggleCategory(
    current: Set<LocationCategory>,
    category: LocationCategory
): Set<LocationCategory> =
    if (category in current) current - category else current + category

private fun LocationCategory.displayName(): String = when (this) {
    LocationCategory.GASTRONOMIE      -> "Gastronomie"
    LocationCategory.PARKPLATZ        -> "Parkplätze"
    LocationCategory.NURBURGRING      -> "Nürburgring"
    LocationCategory.RACETAXI         -> "Racetaxi"
    LocationCategory.AUTOVERMIETUNG   -> "Autovermietung"
    LocationCategory.TANKSTELLE       -> "Tankstellen"
    LocationCategory.KULTUR           -> "Kultur"
    LocationCategory.STRECKENQUERUNG  -> "Streckenquerungen"
    LocationCategory.LADESAEULE       -> "Ladesäulen"
}