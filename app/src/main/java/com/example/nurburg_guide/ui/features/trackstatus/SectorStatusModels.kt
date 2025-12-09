package com.example.nurburg_guide.ui.features.trackstatus

enum class SectorStatus {
    GREEN,
    YELLOW,
    RED
}

data class SectorState(
    val sectorId: Int,
    val status: SectorStatus
)