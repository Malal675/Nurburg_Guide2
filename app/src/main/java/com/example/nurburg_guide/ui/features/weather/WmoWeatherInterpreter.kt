package com.example.nurburg_guide.ui.features.weather

/**
 * Kurze Beschreibung + Detailtext + Emoji für einen WMO-WeatherCode.
 * Quelle Codes: Open-Meteo / WMO Weather interpretation codes.
 */
data class WeatherDescription(
    val emoji: String,
    val short: String,
    val detail: String
)

/**
 * Übersetzt den numerischen Wettercode (z.B. 75) in eine lesbare Beschreibung.
 */
fun interpretWeatherCode(code: Int?): WeatherDescription {
    if (code == null) {
        return WeatherDescription(
            emoji = "❓",
            short = "Unbekannt",
            detail = "Kein Wettercode verfügbar"
        )
    }

    return when (code) {
        0 -> WeatherDescription("☀️", "Klarer Himmel", "Keine Wolken, perfekte Sicht")
        1, 2, 3 -> WeatherDescription("⛅", "Wolkig", "Überwiegend freundlich, einzelne Wolkenfelder")

        45, 48 -> WeatherDescription("🌫️", "Nebel", "Sicht eingeschränkt durch Nebel oder Dunst")

        51, 53, 55 -> WeatherDescription("🌦️", "Nieselregen", "Leichter bis mäßiger Nieselregen")
        56, 57 -> WeatherDescription("🌧️", "Gefrierender Nieselregen", "Rutschige Bedingungen möglich")

        61, 63, 65 -> WeatherDescription("🌧️", "Regen", "Leichter bis starker Regen")
        66, 67 -> WeatherDescription("🌧️❄️", "Gefrierender Regen", "Sehr rutschig, erhöhte Unfallgefahr")

        71, 73, 75 -> WeatherDescription("❄️", "Schneefall", "Leichter bis starker Schneefall")
        77 -> WeatherDescription("❄️", "Schneegriesel", "Feine Schneekörner")

        80, 81, 82 -> WeatherDescription("🌦️", "Regenschauer", "Kurzzeitige Schauer, Intensität variabel")
        85, 86 -> WeatherDescription("❄️", "Schneeschauer", "Kurzzeitige Schneeschauer")

        95 -> WeatherDescription("⛈️", "Gewitter", "Gewitter mit Blitz und Donner")
        96, 99 -> WeatherDescription("⛈️🌨️", "Gewitter mit Hagel", "Starke Gewitter, Hagel möglich")

        else -> WeatherDescription(
            emoji = "❓",
            short = "Unbekannt",
            detail = "Wettercode $code ist nicht explizit abgedeckt"
        )
    }
}
