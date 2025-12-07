package com.example.nurburg_guide.ui.features.map

// Android / System
import android.annotation.SuppressLint
import android.location.Location

// Compose UI
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.google.maps.android.compose.rememberCameraPositionState

// Maplocations
import com.example.nurburg_guide.ui.features.map.data.AllLocations
import com.example.nurburg_guide.ui.features.map.model.GuideLocation

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

    // Startposition: Nürburgring
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(50.36499, 6.97163),
            12.5f
        )
    }

    var selectedLocation by remember { mutableStateOf<GuideLocation?>(null) }
    val coroutineScope = rememberCoroutineScope()

    // Location-Request-Konfiguration
    val locationRequest = remember {
        LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            5_000L            // Intervall in ms
        )
            .setMinUpdateIntervalMillis(2_000L)
            .build()
    }

    // Listener, der Updates in userLocation schreibt
    val locationListener = remember {
        object : LocationListener {
            override fun onLocationChanged(location: Location) {
                userLocation = LatLng(location.latitude, location.longitude)
            }
        }
    }

    // fortlaufende Updates + einmaliger letzter Standort
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
        } catch (se: SecurityException) {
            // Falls noch keine Location-Permission erteilt wurde:
            // wir ignorieren das erstmal, Map funktioniert trotzdem.
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // ✅ Nur EINE GoogleMap mit DIESEM cameraPositionState
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(
                myLocationButtonEnabled = false    // wir haben unseren eigenen Button
            )
        ) {
            // Alle Locations als Marker
            AllLocations.all.forEach { location ->
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

        // FloatingActionButton zum auf Nutzerposition zentrieren
        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .offset(x = (-40).dp),   // Abstand von den Zoom-Controls
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

        // TODO: Hier können wir später noch eine Card für selectedLocation unten einblenden
    }
}