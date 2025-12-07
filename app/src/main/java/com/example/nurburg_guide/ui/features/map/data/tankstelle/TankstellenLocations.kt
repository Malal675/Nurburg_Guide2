package com.example.nurburg_guide.ui.features.map.data.tankstelle

import com.example.nurburg_guide.ui.features.map.model.GuideLocation
import com.example.nurburg_guide.ui.features.map.model.LocationCategory

object TankstellenLocations {

    val all: List<GuideLocation> = listOf(
        GuideLocation(
            id = "tankstelle_dorint",
            name = "Tankstelle am Nürburgring",
            latitude = 50.333000,
            longitude = 6.945000,
            category = LocationCategory.TANKSTELLE,
            url = null
        )
        // weitere Tankstellen hier ergänzen
    )
}