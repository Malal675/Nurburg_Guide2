package com.example.nurburg_guide.ui.features.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen() {
    val nurburg = LatLng(50.3669, 6.9643)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(nurburg, 13f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            state = MarkerState(position = nurburg),
            title = "Nürburgring",
            snippet = "Döttinger Höhe"
        )
    }
}