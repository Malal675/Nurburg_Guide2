package com.example.nurburg_guide.ui.features.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.nurburg_guide.data.trackstatus.SectorState
import com.example.nurburg_guide.data.trackstatus.SectorStatus
import com.example.nurburg_guide.ui.features.map.sectors.TrackSectorsRepository
import com.google.maps.android.compose.Polyline

@Composable
fun TrackSectorsOverlay(
    sectorsState: List<SectorState>
) {
    sectorsState.forEach { sectorState ->
        // unsere neue SectorState hat "id" statt "sectorId"
        val sector = TrackSectorsRepository.getSectorById(sectorState.id)
            ?: return@forEach

        val color = when (sectorState.status) {
            SectorStatus.GREEN  -> Color(0xFF00C853) // Grün
            SectorStatus.YELLOW -> Color(0xFFFFD600) // Gelb
            SectorStatus.RED    -> Color(0xFFD50000) // Rot
        }

        Polyline(
            points = sector.points,
            width = 18f,   // dicke Linie
            color = color
        )
    }
}