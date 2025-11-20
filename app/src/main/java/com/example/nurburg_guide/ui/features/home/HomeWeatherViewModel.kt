package com.example.nurburg_guide.ui.features.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nurburg_guide.data.weather.WeatherNetwork
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * ViewModel für das Wetter auf dem Home-Screen.
 *
 * - Holt beim Erstellen einmal das aktuelle Wetter am Nürburgring.
 * - Hält den Zustand in WeatherUiState als Compose-State (mutableStateOf).
 */
class HomeWeatherViewModel : ViewModel() {

    var uiState by mutableStateOf(WeatherUiState())
        private set

    init {
        loadWeather()
    }

    private fun setState(newState: WeatherUiState) {
        uiState = newState
    }

    /**
     * Von der UI aufrufbar, um die Daten neu zu laden.
     */
    fun refresh() {
        loadWeather()
    }

    private fun loadWeather() {
        // Koordinaten Nürburgring (vereinfacht)
        val latitude = 50.335
        val longitude = 6.947

        setState(
            uiState.copy(
                isLoading = true,
                errorMessage = null
            )
        )

        viewModelScope.launch {
            try {
                val response = WeatherNetwork.openMeteoApi.getCurrentWeather(
                    latitude = latitude,
                    longitude = longitude
                )

                val current = response.current

                if (current != null) {
                    // Uhrzeit für "Zuletzt aktualisiert"
                    val timeString = SimpleDateFormat("HH:mm", Locale.getDefault())
                        .format(Date())

                    setState(
                        WeatherUiState(
                            isLoading = false,
                            temperatureC = current.temperature2m,
                            precipitationMm = current.precipitation,
                            windSpeedKmh = current.windSpeed10m,
                            weatherCode = current.weatherCode,
                            errorMessage = null,
                            lastUpdated = timeString
                        )
                    )
                } else {
                    setState(
                        WeatherUiState(
                            isLoading = false,
                            errorMessage = "Keine aktuellen Wetterdaten verfügbar.",
                            lastUpdated = null
                        )
                    )
                }
            } catch (e: Exception) {
                setState(
                    WeatherUiState(
                        isLoading = false,
                        errorMessage = "Fehler beim Laden der Wetterdaten: ${e.message}",
                        lastUpdated = null
                    )
                )
            }
        }
    }
}
