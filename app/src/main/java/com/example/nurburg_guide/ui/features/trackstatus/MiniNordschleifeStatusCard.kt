package com.example.nurburg_guide.ui.features.trackstatus

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.nurburg_guide.data.trackstatus.SectorState
import com.example.nurburg_guide.ui.features.map.TrackSectorsOverlay
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MiniNordschleifeStatusCard(
    sectorsState: List<SectorState>,
    onReportClick: () -> Unit, // bleibt drin, damit du TrackStatusScreen NICHT ändern musst
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        // ✅ sehr wenig Rand
        Column(Modifier.padding(4.dp)) {

            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(
                    LatLng(50.36, 6.963),
                    12.0f
                )
            }

            // ✅ höher, damit der Ring besser „ganz“ wirkt
            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(mapType = MapType.SATELLITE), // ✅ TrackStatus = Satellite
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = false,
                    compassEnabled = false,
                    myLocationButtonEnabled = false,
                    mapToolbarEnabled = false,
                    scrollGesturesEnabled = false,
                    zoomGesturesEnabled = false,
                    tiltGesturesEnabled = false,
                    rotationGesturesEnabled = false
                )
            ) {
                TrackSectorsOverlay(sectorsState = sectorsState)
            }
        }
    }
}
