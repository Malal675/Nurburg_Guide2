package com.example.nurburg_guide.ui.features.map.data.autovermietung

import com.example.nurburg_guide.ui.features.map.model.GuideLocation
import com.example.nurburg_guide.ui.features.map.model.LocationCategory

object VermietungLocations {

    val all: List<GuideLocation> = listOf(
        GuideLocation(
            id = "auto_big_garage_nuerburg",
            name = "Big Garage Nürburg",
            latitude = 50.33407,
            longitude = 6.94683,
            category = LocationCategory.AUTOVERMIETUNG,
            url = null
        ),
        GuideLocation(
            id = "auto_nuerblife",
            name = "Nürblife",
            latitude = 50.34261,
            longitude = 6.95268,
            category = LocationCategory.AUTOVERMIETUNG,
            url = null
        ),
        GuideLocation(
            id = "auto_rent4ring",
            name = "Rent4Ring",
            latitude = 50.34304,
            longitude = 6.95203,
            category = LocationCategory.AUTOVERMIETUNG,
            url = null
        )
    )
}