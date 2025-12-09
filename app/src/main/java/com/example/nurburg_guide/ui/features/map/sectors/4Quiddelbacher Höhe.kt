package com.example.nurburg_guide.ui.features.map.sectors

import com.example.nurburg_guide.ui.features.map.Sector
import com.google.android.gms.maps.model.LatLng

val QuiddelbacherHoeheSector = Sector(
    id = 4,   // neue eindeutige ID
    name = "Quiddelbacher Höhe",
    points = listOf(
        LatLng(50.34115, 6.93333),
        LatLng(50.34176, 6.93208),
        LatLng(50.34283, 6.92966),
        LatLng(50.34360, 6.92842),
        LatLng(50.34486, 6.92682),
    )
)