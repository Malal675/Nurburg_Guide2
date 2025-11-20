package com.example.nurburg_guide.data.weather

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit-Interface für die Open-Meteo Forecast API.
 *
 * Wir holen uns:
 * - current temperature_2m
 * - current precipitation
 * - current weather_code
 * - current wind_speed_10m
 *
 * Damit können wir später:
 * - Temperatur anzeigen
 * - Symbol/Beschreibung über weather_code ableiten
 * - Regen/Nässe einschätzen
 * - Windstärke anzeigen
 */
interface OpenMeteoApi {

    @GET("v1/forecast")
    suspend fun getCurrentWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String = "temperature_2m,precipitation,weather_code,wind_speed_10m",
        @Query("timezone") timezone: String = "auto"
    ): OpenMeteoResponse
}
