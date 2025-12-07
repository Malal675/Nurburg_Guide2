package com.example.nurburg_guide.ui.features.map.data.tankstelle

import com.example.nurburg_guide.ui.features.map.model.GuideLocation
import com.example.nurburg_guide.ui.features.map.model.LocationCategory

object TankstellenLocations {

    val all: List<GuideLocation> = listOf(
        GuideLocation(
            id = "tank_ed_doettinger_hoehe",
            name = "ED Tankstelle Döttinger Höhe",
            latitude = 50.35143,
            longitude = 6.98129,
            category = LocationCategory.TANKSTELLE,
            url = null
        ),
        GuideLocation(
            id = "tank_hoffmann_24h_gasoil",
            name = "Hoffmann 24h Gasoil Station",
            latitude = 50.37111,
            longitude = 6.95526,
            category = LocationCategory.TANKSTELLE,
            url = null
        ),
        GuideLocation(
            id = "tank_aral_exmuehle",
            name = "Aral Tankstelle Exmühle",
            latitude = 50.37803,
            longitude = 6.94925,
            category = LocationCategory.TANKSTELLE,
            url = null
        ),
        GuideLocation(
            id = "tank_ed_leimbach",
            name = "ED Tankstelle Leimbach",
            latitude = 50.40289,
            longitude = 6.92421,
            category = LocationCategory.TANKSTELLE,
            url = null
        )
    )
}