package com.example.nurburg_guide.ui.features.map

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@Composable
fun MapScreen() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // FusedLocationClient für letzte bekannte Position
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    // Nürburg / Döttinger Höhe als Fallback
    val nurburg = LatLng(50.3669, 6.9643)

    // Kamera-Startposition
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(nurburg, 13f)
    }

    // Merker für letzte bekannte User-Position
    var lastKnownLocation by remember { mutableStateOf<LatLng?>(null) }

    // Prüfen, ob Standortberechtigung vorhanden ist
    val hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Einmalig letzte bekannte Position laden (wenn erlaubt)
    LaunchedEffect(hasLocationPermission) {
        if (hasLocationPermission) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    lastKnownLocation = LatLng(location.latitude, location.longitude)
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = hasLocationPermission
            ),
            uiSettings = MapUiSettings(
                myLocationButtonEnabled = false // wir bauen unseren eigenen Button
            )
        ) {
            // Marker am Ring (bleibt als Orientierung)
            Marker(
                state = MarkerState(position = nurburg),
                title = "Nürburgring",
                snippet = "Döttinger Höhe"
            )
        }

        // „Zu mir zentrieren“-Button nur, wenn wir Standort nutzen dürfen
        if (hasLocationPermission) {
            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                onClick = {
                    val target = lastKnownLocation ?: nurburg
                    coroutineScope.launch {
                        cameraPositionState.animate(
                            CameraUpdateFactory.newLatLngZoom(target, 15f),
                            1000
                        )
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.MyLocation,
                    contentDescription = "Zu mir zentrieren"
                )
            }
        } else {
            // Kleiner Text-Hinweis, falls keine Berechtigung (optional)
            Text(
                text = "Standortfreigabe nötig, um deine Position zu zeigen.",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
            )
        }
    }
}