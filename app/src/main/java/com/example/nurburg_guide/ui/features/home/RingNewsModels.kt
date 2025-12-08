package com.example.nurburg_guide.ui.features.home

/**
 * Kategorie für News auf der Explore/Home-Page.
 */
enum class RingNewsCategory {
    LAP_RECORD,
    EVENT,
    GENERAL,
}

/**
 * Einfaches Datenmodell für einen News-Eintrag.
 */
data class RingNewsItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val category: RingNewsCategory,
    val dateLabel: String,
    val source: String,
    val imageUrl: String? = null,
    val articleUrl: String? = null,
)

/**
 * Lesbares Label für die Kategorie.
 */
fun RingNewsCategory.label(): String = when (this) {
    RingNewsCategory.LAP_RECORD -> "Rundenrekord"
    RingNewsCategory.EVENT      -> "Event"
    RingNewsCategory.GENERAL    -> "News"
}

/**
 * Statischer Demo-Feed – später können wir das durch eine API ersetzen.
 *
 * Enthält:
 *  - Manthey Porsche 911 GT3 Laprecord
 *  - Essen Motor Show Nürburgring-Stand
 */
fun sampleRingNews(): List<RingNewsItem> = listOf(
    RingNewsItem(
        id = "laprecord-manthey-gt3-2024",
        title = "Porsche 911 GT3 mit Manthey Kit: 6:52.981 min auf der Nordschleife",
        subtitle = "Manthey Performance Kit, 20,8 km Nordschleife – offizielle Zeitmessung inklusive Start/Ziel-Passage.",
        category = RingNewsCategory.LAP_RECORD,
        dateLabel = "2024",
        source = "Nürburgring / Manthey",
        imageUrl = null, // Wenn du ein passendes Bild hast, hier URL eintragen
        articleUrl = "https://nuerburgring.de/news/porsche-911-gt3-mit-manthey-kit-die-nordschleife-in-6-52-981-minuten",
    ),
    RingNewsItem(
        id = "event-essen-motorshow-stand",
        title = "Nürburgring auf der Essen Motor Show",
        subtitle = "Live-Eindrücke vom Nürburgring-Stand: GT3-Boliden, Community-Vibes und Programm rund um die Nordschleife.",
        category = RingNewsCategory.EVENT,
        dateLabel = "Essen Motor Show",
        source = "Nürburgring / YouTube",
        // Beispielbild aus dem YouTube-Stream
        imageUrl = "https://img.youtube.com/vi/8GorSTpDI0Y/maxresdefault.jpg",
        // passendes offizielles Video:
        articleUrl = "https://www.youtube.com/watch?v=8GorSTpDI0Y",
    ),
)
