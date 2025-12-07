package com.example.nurburg_guide.ui.features.map.data.taxi

import com.example.nurburg_guide.ui.features.map.model.GuideLocation
import com.example.nurburg_guide.ui.features.map.model.LocationCategory

object TaxiLocations {

    val all: List<GuideLocation> = listOf(
        GuideLocation(
            id = "taxi_getspeed_racetaxi",
            name = "GetSpeed Racetaxi",
            latitude = 50.33407,
            longitude = 6.94683,
            category = LocationCategory.RACETAXI,
            url = null
        ),
        GuideLocation(
            id = "taxi_hyundai_driving_experience",
            name = "Hyundai Driving Experience",
            latitude = 50.34630,
            longitude = 6.96525,
            category = LocationCategory.RACETAXI,
            url = null
        ),
        GuideLocation(
            id = "taxi_ringtaxi_gp",
            name = "Ringtaxi GP-Strecke",
            latitude = 50.33407,
            longitude = 6.94683,
            category = LocationCategory.RACETAXI,
            url = null
        ),
        GuideLocation(
            id = "taxi_ringtaxi_nordschleife",
            name = "Ringtaxi Nordschleife",
            latitude = 50.34664,
            longitude = 6.96636,
            category = LocationCategory.RACETAXI,
            url = null
        )
    )
}