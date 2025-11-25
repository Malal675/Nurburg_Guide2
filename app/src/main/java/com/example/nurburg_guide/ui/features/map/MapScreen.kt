package com.example.nurburg_guide.ui.features.map

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.offset
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
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
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
            LatLng(50.3350, 6.9470),
            13f
        )
    }

    // Einmalig aktuelle Position holen (du hast die Permissions ja schon)
    LaunchedEffect(Unit) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                userLocation = LatLng(location.latitude, location.longitude)
            }
        }
    }

    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {

        // --- Karte ---
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = true
            ),
            uiSettings = MapUiSettings(
                myLocationButtonEnabled = false   // eigenen Button verwenden
            )
        )

        // --- "Zu mir zentrieren"-Button ---
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
            modifier = Modifier
                .align(Alignment.BottomEnd)  // Button bleibt unten rechts
                .padding(120.dp)  // Padding nach oben
                .offset(x = 105.dp)  // Button weiter nach rechts verschieben
        ) {
            Icon(
                imageVector = Icons.Filled.MyLocation,
                contentDescription = "Zu mir zentrieren"
            )
        }
    }
}