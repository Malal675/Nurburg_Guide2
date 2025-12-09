package com.example.nurburg_guide.ui.features.map.sectors

import com.example.nurburg_guide.ui.features.map.Sector
import com.google.android.gms.maps.model.LatLng

val FlugplatzSector = Sector(
    id = 5,   // nächste eindeutige ID
    name = "Flugplatz",
    points = listOf(
        LatLng(50.34486, 6.92682),
        LatLng(50.34553, 6.92603),
        LatLng(50.34618, 6.92585),
        LatLng(50.34675, 6.92585),
        LatLng(50.34785, 6.92656),
    )
)