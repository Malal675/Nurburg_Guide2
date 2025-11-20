package com.example.nurburg_guide.ui.features.trackstatus

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nurburg_guide.data.trackstatus.InMemoryTrackStatusRepository
import com.example.nurburg_guide.data.trackstatus.TrackSectionStatus
import com.example.nurburg_guide.data.trackstatus.TrackStatusRepository
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class TrackStatusUiState(
    val isLoading: Boolean = true,
    val sections: List<TrackSectionStatus> = emptyList(),
    val isTrackRed: Boolean = false          // 🔴 neu
)

class TrackStatusViewModel : ViewModel() {

    private val repository: TrackStatusRepository = InMemoryTrackStatusRepository(viewModelScope)

    var uiState by mutableStateOf(TrackStatusUiState())
        private set

    init {
        observeState()
    }

    private fun observeState() {
        viewModelScope.launch {
            combine(
                repository.trackSections,
                repository.isTrackRed
            ) { sections, isRed ->
                TrackStatusUiState(
                    isLoading = false,
                    sections = sections,
                    isTrackRed = isRed
                )
            }.collect { newState ->
                uiState = newState
            }
        }
    }

    fun onReportYellow(sectionId: String) {
        viewModelScope.launch {
            repository.reportYellow(sectionId)
        }
    }

    // 🔴 neu: Rot setzen/aufheben
    fun setTrackRed(enabled: Boolean) {
        viewModelScope.launch {
            repository.setTrackRed(enabled)
        }
    }
}
