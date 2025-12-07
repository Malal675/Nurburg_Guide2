package com.example.nurburg_guide.ui.features.map.data.parkplatz

import com.example.nurburg_guide.ui.features.map.model.GuideLocation
import com.example.nurburg_guide.ui.features.map.model.LocationCategory

object ParkplatzLocations {

    val all: List<GuideLocation> = listOf(
        GuideLocation(
            id = "parkplatz_hauptparkplatz",
            name = "Hauptparkplatz am Ring",
            latitude = 50.334000,
            longitude = 6.948000,
            category = LocationCategory.PARKPLATZ,
            url = null
        )
    )
}