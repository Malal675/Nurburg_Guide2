package com.example.nurburg_guide.ui.features.home

/**
 * UI-Zustand für die Wetteranzeige auf dem Home-Screen.
 *
 * - isLoading: true, solange wir Daten laden
 * - errorMessage: nicht-null, wenn etwas schiefgeht
 * - temperatureC: Temperatur in °C
 * - precipitationMm: Niederschlag (mm)
 * - windSpeedKmh: Windgeschwindigkeit (km/h)
 * - weatherCode: Code von Open-Meteo
 * - lastUpdated: Uhrzeit der letzten erfolgreichen Aktualisierung (z.B. "15:39")
 */
data class WeatherUiState(
    val isLoading: Boolean = true,
    val temperatureC: Double? = null,
    val precipitationMm: Double? = null,
    val windSpeedKmh: Double? = null,
    val weatherCode: Int? = null,
    val errorMessage: String? = null,
    val lastUpdated: String? = null
)
