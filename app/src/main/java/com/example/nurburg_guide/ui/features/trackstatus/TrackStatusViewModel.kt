package com.example.nurburg_guide.ui.features.trackstatus

import androidx.lifecycle.ViewModel
import com.example.nurburg_guide.data.trackstatus.INITIAL_SECTOR_STATES
import com.example.nurburg_guide.data.trackstatus.SectorState
import com.example.nurburg_guide.data.trackstatus.SectorStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * UI-State für globale Infos (Rot-Banner, Loading, ...)
 */
data class TrackStatusUiState(
    val isTrackRed: Boolean = false,
    val isLoading: Boolean = false
)

class TrackStatusViewModel : ViewModel() {

    // 1..32 initial GRÜN
    private val _sectors = MutableStateFlow(INITIAL_SECTOR_STATES)
    val sectors: StateFlow<List<SectorState>> = _sectors.asStateFlow()

    private val _uiState = MutableStateFlow(TrackStatusUiState())
    val uiState: StateFlow<TrackStatusUiState> = _uiState.asStateFlow()

    private fun updateSector(id: Int, transform: (SectorState) -> SectorState) {
        _sectors.value = _sectors.value.map { sector ->
            if (sector.id == id) transform(sector) else sector
        }
    }

    /** Nutzer meldet GELB für genau einen Sektor */
    fun reportYellow(id: Int) {
        val now = System.currentTimeMillis()
        val fiveMinutes = 5 * 60 * 1000L

        updateSector(id) { old ->
            if (old.status == SectorStatus.RED) {
                old // Rot hat Priorität
            } else {
                old.copy(
                    status = SectorStatus.YELLOW,
                    yellowUntilMillis = now + fiveMinutes
                )
            }
        }
    }

    /** Nutzer meldet ROT für die ganze Strecke (global) */
    private fun reportRedGlobal() {
        val now = System.currentTimeMillis()
        val thirtyMinutes = 30 * 60 * 1000L

        _sectors.value = _sectors.value.map { old ->
            old.copy(
                status = SectorStatus.RED,
                redReportCount = old.redReportCount + 1,
                redUntilMillis = now + thirtyMinutes,
                yellowUntilMillis = null
            )
        }
        _uiState.value = _uiState.value.copy(isTrackRed = true)
    }

    /**
     * Wird vom Screen aufgerufen:
     * - isRed = true  → Strecke rot melden
     * - isRed = false → Rot aufheben
     */
    fun setTrackRed(isRed: Boolean) {
        if (isRed) {
            reportRedGlobal()
        } else {
            // alles zurück auf GRÜN
            _sectors.value = _sectors.value.map { old ->
                old.copy(
                    status = SectorStatus.GREEN,
                    yellowUntilMillis = null,
                    redUntilMillis = null,
                    redReportCount = 0
                )
            }
            _uiState.value = _uiState.value.copy(isTrackRed = false)
        }
    }

    /** Optional: Rot für einzelnen Sektor mit 2-Meldungen-Logik */
    fun reportRedSingleSector(id: Int) {
        val now = System.currentTimeMillis()
        val thirtyMinutes = 30 * 60 * 1000L

        updateSector(id) { old ->
            val newCount = old.redReportCount + 1
            if (newCount >= 2) {
                old.copy(
                    status = SectorStatus.RED,
                    redReportCount = newCount,
                    redUntilMillis = now + thirtyMinutes,
                    yellowUntilMillis = null
                )
            } else {
                old.copy(redReportCount = newCount)
            }
        }
    }

    /** Timer-Logik, wenn du sie später zyklisch aufrufst */
    fun cleanupExpiredStates() {
        val now = System.currentTimeMillis()
        _sectors.value = _sectors.value.map { old ->
            var result = old

            // Gelb abgelaufen?
            val yellowUntil = result.yellowUntilMillis
            if (result.status == SectorStatus.YELLOW &&
                yellowUntil != null &&
                yellowUntil <= now
            ) {
                result = result.copy(
                    status = SectorStatus.GREEN,
                    yellowUntilMillis = null
                )
            }

            // Rot abgelaufen?
            val redUntil = result.redUntilMillis
            if (result.status == SectorStatus.RED &&
                redUntil != null &&
                redUntil <= now
            ) {
                result = result.copy(
                    status = SectorStatus.GREEN,
                    redUntilMillis = null,
                    redReportCount = 0
                )
            }

            result
        }

        if (_sectors.value.all { it.status == SectorStatus.GREEN }) {
            _uiState.value = _uiState.value.copy(isTrackRed = false)
        }
    }
}