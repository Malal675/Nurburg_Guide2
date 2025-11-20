package com.example.nurburg_guide.data.weather

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Zentraler Netzwerk-Client für Wetter.
 *
 * Wichtig:
 * - MainActivity bleibt frei von Retrofit/Moshi/OkHttp
 * - UI nutzt später nur noch WeatherNetwork.openMeteoApi (oder ein Repository).
 */
object WeatherNetwork {

    private const val BASE_URL = "https://api.open-meteo.com/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        // BASIC = Requestlinie + Response-Status
        // Bei Bedarf auf BODY stellen, wenn du alles sehen willst.
        level = HttpLoggingInterceptor.Level.BASIC
    }

    private val httpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val moshi: Moshi = Moshi.Builder()
        // Wichtig, damit Moshi mit Kotlin-Datenklassen gut umgehen kann
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val openMeteoApi: OpenMeteoApi = retrofit.create(OpenMeteoApi::class.java)
}
