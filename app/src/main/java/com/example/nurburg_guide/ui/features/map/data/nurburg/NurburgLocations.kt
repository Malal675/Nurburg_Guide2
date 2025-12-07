package com.example.nurburg_guide.ui.features.map.data.nurburg

import com.example.nurburg_guide.ui.features.map.model.GuideLocation
import com.example.nurburg_guide.ui.features.map.model.LocationCategory

object NurburgLocations {

    val all: List<GuideLocation> = listOf(
        GuideLocation(
            id = "nurburg_haupttribuene",
            name = "Haupttribüne Nürburgring",
            latitude = 50.335000,
            longitude = 6.947000,
            category = LocationCategory.NURBURGRING,
            url = null
        )
        // weitere Nürburgring-Locations hier ergänzen
    )
}