package com.example.nurburg_guide.ui.features.map.data.querungen

import com.example.nurburg_guide.ui.features.map.model.GuideLocation
import com.example.nurburg_guide.ui.features.map.model.LocationCategory

object QuerungenLocations {

    val all: List<GuideLocation> = listOf(
        GuideLocation(
            id = "querung_bruecke_t13",
            name = "Streckenquerung T13",
            latitude = 50.336000,
            longitude = 6.944000,
            category = LocationCategory.STRECKENQUERUNG,
            url = null
        )
        // weitere Querungen hier ergänzen
    )
}