package com.example.nurburg_guide.ui.features.map.spectator

data class SpectatorSpotMeta(
    val title: String,
    val imageName: String?,            // drawable name ohne .png/.jpg
    val suggestedParkingName: String?, // exakt wie in eurer Parkplatz-Liste/Excel
    val parkingHint: String
)

object SpectatorSpotMetaStore {

    private fun meta(title: String, parking: String) = SpectatorSpotMeta(
        title = title,
        imageName = "sp_${title.toSpotKey()}",
        suggestedParkingName = parking,
        parkingHint = "Empfehlung: $parking"
    )

    private val all = listOf(
        meta("Nordschleife Zufahrt", "Parkplatz Einfahrt Nordschleife"),
        meta("Bridge (Antoniusweg)", "Parkplatz Einfahrt Nordschleife"),
        meta("T13", "Parkplatz T13"),
        meta("Sabine Schmitz-Kurve", "Parkplatz T13"),
        meta("Hatzenbach", "Parkplatz Quiddelbacher-Höhe"),
        meta("Hocheichen", "Parkplatz Quiddelbacher-Höhe"),
        meta("Adenauer Forst (Youtube Chicane)", "Parkplatz Adenauer Forst"),
        meta("Wehrseifen", "Parkplatz Breidscheid"),
        meta("Breidscheid", "Parkplatz Breidscheid"),
        meta("Caracciola-Karussell", "Parkplatz Brünnchen"),
        meta("Hohe Acht", "Parkplatz Brünnchen"),
        meta("Wippermann", "Parkplatz Brünnchen"),
        meta("Brünnchen", "Parkplatz Brünnchen"),
        meta("Youtube Corner", "Parkplatz Brünnchen"),
        meta("Pflanzgarten", "Parkplatz Pflanzgarten"),
        meta("Kleines Karussell", "Parkplatz kleines Karussell"),
    )

    private val byKey: Map<String, SpectatorSpotMeta> =
        all.associateBy { it.title.toSpotKey() }

    fun metaForSpotName(spotName: String): SpectatorSpotMeta? {
        val key = spotName.toSpotKey()
        byKey[key]?.let { return it }

        // Fallback für häufige Schreibweisen/Tippos
        val fixedKey = key
            .replace("carricaloula", "caracciola")
            .replace("caraciola", "caracciola")
            .replace("karussel", "karussell")

        byKey[fixedKey]?.let { return it }

        // Extra-Fallback: eindeutig "großes Caracciola-Karussell"
        if (spotName.contains("caracci", ignoreCase = true) &&
            spotName.contains("karuss", ignoreCase = true)
        ) {
            return byKey["caracciola_karussell"]
        }

        // Extra-Fallback: "kleines Karussell"
        if (spotName.contains("kleines", ignoreCase = true) &&
            spotName.contains("karuss", ignoreCase = true)
        ) {
            return byKey["kleines_karussell"]
        }

        return null
    }
}

fun String.toSpotKey(): String {
    val s = lowercase()
        .replace("ä", "ae").replace("ö", "oe").replace("ü", "ue").replace("ß", "ss")
    return s
        .replace(Regex("[^a-z0-9]+"), "_")
        .replace(Regex("_+"), "_")
        .trim('_')
}
