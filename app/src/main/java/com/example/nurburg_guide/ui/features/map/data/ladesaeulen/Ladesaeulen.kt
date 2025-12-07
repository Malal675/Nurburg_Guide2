package com.example.nurburg_guide.ui.features.map.data.ladesaeulen

import com.example.nurburg_guide.ui.features.map.model.GuideLocation
import com.example.nurburg_guide.ui.features.map.model.LocationCategory

object LadesaeulenLocations {

    val all: List<GuideLocation> = listOf(
        GuideLocation(
            id = "lade_tesla_supercharger",
            name = "Tesla Supercharger",
            latitude = 50.33138,
            longitude = 6.94525,
            category = LocationCategory.LADESAEULE,
            url = null
        ),
        GuideLocation(
            id = "lade_totalenergies_ev_charge",
            name = "TotalEnergies EV Charge",
            latitude = 50.33314,
            longitude = 6.94757,
            category = LocationCategory.LADESAEULE,
            url = null
        ),
        GuideLocation(
            id = "lade_supercharger_xxxx",
            name = "Supercharger XXXX",
            latitude = 50.33831,
            longitude = 6.95017,
            category = LocationCategory.LADESAEULE,
            url = null
        )
    )
}