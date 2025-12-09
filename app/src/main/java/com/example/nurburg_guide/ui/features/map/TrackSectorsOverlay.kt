package com.example.nurburg_guide.ui.features.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.maps.android.compose.Polyline
import com.example.nurburg_guide.ui.features.map.sectors.TrackSectorsRepository
import com.example.nurburg_guide.ui.features.trackstatus.SectorState
import com.example.nurburg_guide.ui.features.trackstatus.SectorStatus

@Composable
fun TrackSectorsOverlay(
    sectorsState: List<SectorState>
) {
    sectorsState.forEach { sectorState ->
        val sector = TrackSectorsRepository.getSectorById(sectorState.sectorId)
            ?: return@forEach

        val color = when (sectorState.status) {
            SectorStatus.GREEN  -> Color(0xFF00C853)
            SectorStatus.YELLOW -> Color(0xFFFFD600)
            SectorStatus.RED    -> Color(0xFFD50000)
        }

        Polyline(
            points = sector.points,
            width = 18f,   // hier: dicke Linie
            color = color
        )
    }
}