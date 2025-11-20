package com.example.nurburg_guide.data.weather

import com.squareup.moshi.Json

/**
 * Datenmodell für die Open-Meteo Forecast API.
 * Wir nutzen hier nur einen Teil der Felder, den wir für die App brauchen.
 *
 * Beispiel-Response laut Doku:
 * - Top-Level: latitude, longitude, timezone, current_units, current, ...
 * - current: time, interval, temperature_2m, precipitation, weather_code, wind_speed_10m, ...
 */

// Units der aktuellen Werte (z.B. "°C", "mm", "km/h")
data class OpenMeteoCurrentUnits(
    val time: String? = null,
    val interval: String? = null,
    @Json(name = "temperature_2m") val temperature2m: String? = null,
    @Json(name = "precipitation") val precipitation: String? = null,
    @Json(name = "weather_code") val weatherCode: String? = null,
    @Json(name = "wind_speed_10m") val windSpeed10m: String? = null
)

// Aktuelle Wetterwerte
data class OpenMeteoCurrent(
    val time: String? = null,
    val interval: Int? = null,
    @Json(name = "temperature_2m") val temperature2m: Double? = null,
    val precipitation: Double? = null,
    @Json(name = "weather_code") val weatherCode: Int? = null,
    @Json(name = "wind_speed_10m") val windSpeed10m: Double? = null
)

// Gesamt-Response
data class OpenMeteoResponse(
    val latitude: Double? = null,
    val longitude: Double? = null,
    @Json(name = "generationtime_ms") val generationTimeMs: Double? = null,
    @Json(name = "utc_offset_seconds") val utcOffsetSeconds: Int? = null,
    val timezone: String? = null,
    @Json(name = "timezone_abbreviation") val timezoneAbbreviation: String? = null,
    val elevation: Double? = null,
    @Json(name = "current_units") val currentUnits: OpenMeteoCurrentUnits? = null,
    val current: OpenMeteoCurrent? = null
)
