package com.example.nurburg_guide.ui.features.map.data.gastronomie

import com.example.nurburg_guide.ui.features.map.model.GuideLocation
import com.example.nurburg_guide.ui.features.map.model.LocationCategory

object GastronomieLocations {

    val all: List<GuideLocation> = listOf(
        GuideLocation(
            id = "gastro_pizzeria_luca",
            name = "Pizzeria Luca",
            latitude = 50.335000,
            longitude = 6.949000,
            category = LocationCategory.GASTRONOMIE,
            url = "https://beispiel-link.de"
        )
        // hier einfach weitere Locations anhängen
    )
}