package com.example.nurburg_guide.ui.features.map.data.taxi

import com.example.nurburg_guide.ui.features.map.model.GuideLocation
import com.example.nurburg_guide.ui.features.map.model.LocationCategory

object TaxiLocations {

    val all: List<GuideLocation> = listOf(
        GuideLocation(
            id = "taxi_ringtaxi",
            name = "Race Taxi Nürburgring",
            latitude = 50.335500,
            longitude = 6.946500,
            category = LocationCategory.RACETAXI,
            url = null
        )
        // weitere Race-Taxi-/Taxi-Locations hier ergänzen
    )
}