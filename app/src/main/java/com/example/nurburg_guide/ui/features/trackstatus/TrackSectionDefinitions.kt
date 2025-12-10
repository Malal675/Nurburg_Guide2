package com.example.nurburg_guide.data.trackstatus

/**
 * Feste Definition eines Streckenabschnitts der Nordschleife.
 *
 * [code] = technischer Schlüssel
 * [displayName] = UI-Name
 *
 * Index + 1 in [NORDSCHLEIFE_SECTIONS] = Sektor-ID (1..32)
 */
data class TrackSectionDefinition(
    val code: String,
    val displayName: String
)

val NORDSCHLEIFE_SECTIONS: List<TrackSectionDefinition> = listOf(
    TrackSectionDefinition("tiergarten", "Tiergarten"),
    TrackSectionDefinition("t13", "T13"),
    TrackSectionDefinition("hatzenbach", "Hatzenbach"),
    TrackSectionDefinition("quiddelbacher_hoehe", "Quiddelbacher Höhe"),
    TrackSectionDefinition("flugplatz", "Flugplatz"),
    TrackSectionDefinition("schwedenkreuz", "Schwedenkreuz"),
    TrackSectionDefinition("aremberg", "Aremberg"),
    TrackSectionDefinition("fuchsroehre", "Fuchsröhre"),
    TrackSectionDefinition("adenauer_forst", "Adenauer Forst"),
    TrackSectionDefinition("metzgesfeld", "Metzgesfeld"),
    TrackSectionDefinition("kallenhard", "Kallenhard"),
    TrackSectionDefinition("wehrseifen", "Wehrseifen"),
    TrackSectionDefinition("breidscheid", "Breidscheid"),
    TrackSectionDefinition("exmuehle", "ExMühle"),
    TrackSectionDefinition("lauda_links", "Lauda-Links"),
    TrackSectionDefinition("bergwerk", "Bergwerk"),
    TrackSectionDefinition("kesselchen", "Kesselchen"),
    TrackSectionDefinition("klostertal", "Klostertal"),
    TrackSectionDefinition("caracciola_karussell", "Caracciola-Karussell"),
    TrackSectionDefinition("hohe_acht", "Hohe Acht"),
    TrackSectionDefinition("hedwigshoehe", "Hedwigshöhe"),
    TrackSectionDefinition("wippermann", "Wippermann"),
    TrackSectionDefinition("eschbach", "Eschbach"),
    TrackSectionDefinition("bruennchen", "Brünnchen"),
    TrackSectionDefinition("eiskurve", "Eiskurve"),
    TrackSectionDefinition("pflanzgarten1", "Pflanzgarten I"),
    TrackSectionDefinition("pflanzgarten2", "Pflanzgarten II"),
    TrackSectionDefinition("stefan_bellof_s", "Stefan-Bellof-S"),
    TrackSectionDefinition("schwalbenschwanz", "Schwalbenschwanz"),
    TrackSectionDefinition("kleines_karussell", "kleines Karussell"),
    TrackSectionDefinition("galgenkopf", "Galgenkopf"),
    TrackSectionDefinition("doettinger_hoehe", "Döttinger Höhe")
)

/**
 * Status eines Sektors (für UI + Map).
 */
enum class SectorStatus {
    GREEN,
    YELLOW,
    RED
}

/**
 * Laufender Zustand eines Sektors (inkl. Timer).
 *
 * [id] = 1..32 = Index in NORDSCHLEIFE_SECTIONS + 1
 */
data class SectorState(
    val id: Int,
    val status: SectorStatus = SectorStatus.GREEN,
    val yellowUntilMillis: Long? = null,
    val redUntilMillis: Long? = null,
    val redReportCount: Int = 0
)

/**
 * Initial: alle 32 Sektoren GRÜN.
 */
val INITIAL_SECTOR_STATES: List<SectorState> =
    NORDSCHLEIFE_SECTIONS.mapIndexed { index, _ ->
        SectorState(id = index + 1)
    }