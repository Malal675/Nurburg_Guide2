package com.example.nurburg_guide.data.trackstatus

import kotlinx.coroutines.flow.StateFlow

interface TrackStatusRepository {

    val trackSections: StateFlow<List<TrackSectionStatus>>

    // 🔴 Neu: globaler Track-Status
    val isTrackRed: StateFlow<Boolean>

    suspend fun reportYellow(sectionId: String)

    // 🔴 Neu: Rot setzen/aufheben
    suspend fun setTrackRed(enabled: Boolean)
}
