package com.example.nurburg_guide.data.trackstatus

/**
 * Feste Definition eines Streckenabschnitts der Nordschleife.
 * Nur ID + Anzeigename, keine Status-Infos.
 */
data class TrackSectionDefinition(
    val id: String,
    val displayName: String
)

/**
 * Reihenfolge = Fahrtrichtung im Turn.
 */
val NORDSCHLEIFE_SECTIONS: List<TrackSectionDefinition> = listOf(
    TrackSectionDefinition("einfahrt", "Einfahrt"),
    TrackSectionDefinition("antoniusbuche", "Antoniusbuche"),
    TrackSectionDefinition("tiergarten", "Tiergarten"),

    TrackSectionDefinition("t13", "T13"),

    TrackSectionDefinition("hatzenbach", "Hatzenbach"),

    TrackSectionDefinition("hocheichen", "Hocheichen"),
    TrackSectionDefinition("quiddelbacher_hoehe", "Quiddelbacher Höhe"),

    TrackSectionDefinition("flugplatz", "Flugplatz"),

    TrackSectionDefinition("schwedenkreuz", "Schwedenkreuz"),
    TrackSectionDefinition("aremberg", "Aremberg"),

    TrackSectionDefinition("fuchsroehre", "Fuchsröhre"),
    TrackSectionDefinition("kompression", "Kompression"),
    TrackSectionDefinition("adenauer_forst", "Adenauer Forst"),

    TrackSectionDefinition("metzgesfeld", "Metzgesfeld"),

    TrackSectionDefinition("kallenhard", "Kallenhard"),
    TrackSectionDefinition("spiegelkurve", "Spiegelkurve"),
    TrackSectionDefinition("miss_hit_miss", "Miss-Hit-Miss"),

    TrackSectionDefinition("wehrseifen", "Wehrseifen"),
    TrackSectionDefinition("breidscheid", "Breidscheid"),
    TrackSectionDefinition("exmuehle", "ExMühle"),

    TrackSectionDefinition("bergwerk", "Bergwerk"),

    TrackSectionDefinition("kesselchen", "Kesselchen"),

    TrackSectionDefinition("mutkurve", "Mutkurve"),
    TrackSectionDefinition("klostertal", "Klostertal"),

    TrackSectionDefinition("klostertalkurve", "Klostertalkurve"),
    TrackSectionDefinition("steilstrecke", "Steilstrecke"),
    TrackSectionDefinition("caracciola_karussell", "Caracciola-Karussell"),

    TrackSectionDefinition("hohe_acht", "Hohe Acht"),

    TrackSectionDefinition("hedwigshoehe", "Hedwigshöhe"),
    TrackSectionDefinition("wippermann", "Wippermann"),
    TrackSectionDefinition("eschbach", "Eschbach"),

    TrackSectionDefinition("bruennchen", "Brünnchen"),
    TrackSectionDefinition("eiskurve", "Eiskurve"),
    TrackSectionDefinition("pflanzgarten", "Pflanzgarten"),

    TrackSectionDefinition("stefan_bellof_s", "Stefan-Bellof-S"),

    TrackSectionDefinition("schwalbenschwanz", "Schwalbenschwanz"),
    TrackSectionDefinition("kleines_karussell", "kleines Karussell"),

    TrackSectionDefinition("galgenkopf", "Galgenkopf"),

    TrackSectionDefinition("doettinger_hoehe", "Döttinger Höhe")
)
