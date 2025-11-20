package com.example.nurburg_guide.data.trackstatus

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val YELLOW_DURATION_MS: Long = 4 * 60 * 1000L // 4 Minuten

class InMemoryTrackStatusRepository(
    private val scope: CoroutineScope
) : TrackStatusRepository {

    private val _trackSections = MutableStateFlow(
        initialTrackSectionStatus()
    )
    override val trackSections: StateFlow<List<TrackSectionStatus>> = _trackSections

    // 🔴 Neu: globaler Rot-Status
    private val _isTrackRed = MutableStateFlow(false)
    override val isTrackRed: StateFlow<Boolean> = _isTrackRed

    private val yellowJobs: MutableMap<String, Job> = mutableMapOf()

    override suspend fun reportYellow(sectionId: String) {
        _trackSections.update { currentList ->
            currentList.map { section ->
                if (section.id == sectionId) {
                    section.copy(status = SectionStatus.YELLOW)
                } else {
                    section
                }
            }
        }

        yellowJobs[sectionId]?.cancel()

        val job = scope.launch {
            delay(YELLOW_DURATION_MS)
            _trackSections.update { currentList ->
                currentList.map { section ->
                    if (section.id == sectionId) {
                        section.copy(status = SectionStatus.GREEN)
                    } else {
                        section
                    }
                }
            }
        }

        yellowJobs[sectionId] = job
    }

    override suspend fun setTrackRed(enabled: Boolean) {
        _isTrackRed.value = enabled
    }
}
