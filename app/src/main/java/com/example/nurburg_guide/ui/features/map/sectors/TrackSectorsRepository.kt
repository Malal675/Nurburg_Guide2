package com.example.nurburg_guide.ui.features.map.sectors

import com.example.nurburg_guide.ui.features.map.Sector

object TrackSectorsRepository {

    // Hier alle Sektoren eintragen
    val allSectors: List<Sector> = listOf(
        TiergartenSector,



        // später: HatzenbachSector, ...
    )

    private val sectorsById = allSectors.associateBy { it.id }

    fun getSectorById(id: Int): Sector? = sectorsById[id]
}