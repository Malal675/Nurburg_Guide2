package com.example.nurburg_guide.ui.features.map.data.nurburg

import com.example.nurburg_guide.ui.features.map.model.GuideLocation
import com.example.nurburg_guide.ui.features.map.model.LocationCategory

object NurburgLocations {

    val all: List<GuideLocation> = listOf(
        GuideLocation(
            id = "nurburg_nordschleife_einfahrt",
            name = "Nordschleife Einfahrt",
            latitude = 50.34601,
            longitude = 6.96573,
            category = LocationCategory.NURBURGRING,
            url = null
        ),
        GuideLocation(
            id = "nurburg_nordschleife_ticketverkauf",
            name = "Nordschleife Touristenfahrten Ticketverkauf",
            latitude = 50.34716,
            longitude = 6.96587,
            category = LocationCategory.NURBURGRING,
            url = null
        ),
        GuideLocation(
            id = "nurburg_ringwerk",
            name = "ring°werk",
            latitude = 50.33470,
            longitude = 6.94795,
            category = LocationCategory.NURBURGRING,
            url = null
        ),
        GuideLocation(
            id = "nurburg_boxengasse",
            name = "Nürburgring Boxengasse",
            latitude = 50.33489,
            longitude = 6.94540,
            category = LocationCategory.NURBURGRING,
            url = null
        ),
        GuideLocation(
            id = "nurburg_gp_strecke",
            name = "Nürburgring Grand-Prix Strecke",
            latitude = 50.33396,
            longitude = 6.94102,
            category = LocationCategory.NURBURGRING,
            url = null
        ),
        GuideLocation(
            id = "nurburg_bilstein_kreisel",
            name = "Bilstein-Kreisel Industriegebiet am Nürburgring",
            latitude = 50.35023,
            longitude = 6.98241,
            category = LocationCategory.NURBURGRING,
            url = null
        )
    )
}