package com.example.nurburg_guide.ui.features.map.sectors

import com.example.nurburg_guide.ui.features.map.Sector

object TrackSectorsRepository {

    val allSectors: List<Sector> = listOf(
        TiergartenSector,            // 1
        T13Sector,                   // 2
        HatzenbachSector,           // 3
        QuiddelbacherHoeheSector,   // 4
        FlugplatzSector,            // 5
        SchwedenkreuzSector,        // 6
        ArembergSector,             // 7
        FuchsroehreSector,          // 8
        AdenauerForstSector,        // 9
        MetzgesfeldSector,          // 10
        KallenhardSector,           // 11
        WehrseifenSector,           // 12
        BreidscheidSector,          // 13
        ExmuehleSector,             // 14
        LaudaLinksSector,           // 15
        BergwerkSector,             // 16
        KesselchenSector,           // 17
        KlostertalSector,           // 18
        CaracciolaKarussellSector,  // 19
        HoheAchtSector,             // 20
        HedwigshoeheSector,         // 21
        WippermannSector,           // 22
        EschbachSector,             // 23
        BruennchenSector,           // 24
        EiskurveSector,             // 25
        Pflanzgarten1Sector,        // 26
        Pflanzgarten2Sector,        // 27
        StefanBellofSSector,        // 28
        SchwalbenschwanzSector,     // 29
        KleinesKarussellSector,     // 30
        GalgenkopfSector,           // 31
        DoettingerHoeheSector,      // 32
    )

    private val sectorsById = allSectors.associateBy { it.id }

    fun getSectorById(id: Int): Sector? = sectorsById[id]
}