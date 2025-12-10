package com.example.nurburg_guide.data.trackstatus

/**
 * Möglicher Status eines Streckenabschnitts.
 * ROT kommt später auf Track-Gesamt-Ebene.
 */
enum class SectionStatus {
    GREEN,
    YELLOW
}

/**
 * Konkreter Status eines Abschnitts:
 * - id + displayName kommen aus den Definitionen
 * - status: aktuell nur GREEN oder YELLOW
 */
data class TrackSectionStatus(
    val id: String,
    val displayName: String,
    val status: SectionStatus
)

/**
 * Startzustand:
 * Alle Abschnitte sind GRÜN.
 */
fun initialTrackSectionStatus(): List<TrackSectionStatus> =
    NORDSCHLEIFE_SECTIONS.map { definition ->
        TrackSectionStatus(
            id = definition.code,
            displayName = definition.displayName,
            status = SectionStatus.GREEN
        )
    }
