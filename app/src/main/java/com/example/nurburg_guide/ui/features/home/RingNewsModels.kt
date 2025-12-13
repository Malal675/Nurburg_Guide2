package com.example.nurburg_guide.ui.features.home

/**
 * Kategorie für News auf der Explore/Home-Page.
 *
 * Hinweis:
 * Explore wurde "aufgeräumt" -> News & Events sollen aktuell nicht angezeigt werden.
 * Deshalb liefert sampleRingNews() momentan bewusst eine leere Liste.
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
 * AKTUELL DEAKTIVIERT:
 * - Explore/Home soll keine News & Events mehr anzeigen -> leere Liste.
 */
fun sampleRingNews(): List<RingNewsItem> = emptyList()
