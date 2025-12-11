package com.example.nurburg_guide.ui.features.community.domain

enum class PostType(val label: String) {
    NORMAL("Post"),
    QUESTION("Frage"),
    POLL("Umfrage")
}

enum class PostCategory(val label: String) {
    GENERAL("Allgemein"),
    TOURISTENFAHRTEN("Touristenfahrten"),
    TRACKDAYS("Trackdays & Rennen"),
    TECH_SETUP("Setup & Technik"),
    ANREISE_PARKEN("Anreise & Parken"),
    MEDIA("Fotos & Videos"),
    RIDES("Mitfahrgelegenheiten")
}
