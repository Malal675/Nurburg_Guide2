package com.example.nurburg_guide.ui.features.community.domain

import java.time.LocalDateTime

/**
 * Dummy-Repo mit Beispielposts.
 * Später ersetzt du das durch Firebase / eigenes Backend.
 */
class InMemoryRingCommunityRepository : CommunityRepository {

    private val userBen = UserProfile(
        id = "user-ben",
        displayName = "Ben – TF-Junkie",
        avatarInitials = "BE"
    )

    private val userCaro = UserProfile(
        id = "user-caro",
        displayName = "Caro – Green Hell",
        avatarInitials = "CA"
    )

    private val userAnon = UserProfile(
        id = "user-anon",
        displayName = "Ring Neuling",
        avatarInitials = "RN"
    )

    private val userAcs = UserProfile(
        id = "user-acs",
        displayName = "AC Schnitzer M2 Tracktool",
        avatarInitials = "M2"
    )

    override suspend fun getFeed(): List<CommunityPost> {
        val now = LocalDateTime.now()

        // Beispiel-Post mit deinem AC-Schnitzer-M2-Bild
        val postM2 = CommunityPost(
            id = "post-m2-acs",
            author = userAcs,
            createdAt = now.minusHours(5),
            title = "AC Schnitzer BMW M2 G87 – Tracktool zum Mieten",
            content = "Frisch entdeckt: AC Schnitzer BMW M2 G87 als Tracktool bei RSR am Ring. " +
                    "Komplettes Aero-Paket, Cup-Bereifung, KW-Fahrwerk – perfekt zum Testen, " +
                    "bevor man das eigene Auto umbaut.\n\n" +
                    "Artikel: bimmertoday.de – AC Schnitzer: BMW M2 G87 als Tuning-Tracktool zum Mieten.",
            imageUrl = "https://www.bimmertoday.de/wp-content/uploads/2024/04/AC-Schnitzer-BMW-M2-G87-Tuning-Tracktool-Miete-01.jpg",
            likeCount = 64,
            commentCount = 7,
            isLikedByMe = true,
            tag = PostTag.SPOTTING
        )

        val userPost1 = CommunityPost(
            id = "post-1",
            author = userBen,
            createdAt = now.minusHours(3),
            title = "Bestes Zeitfenster für Feierabend-TF?",
            content = "Wann fahrt ihr am liebsten nach Feierabend? Ich finde 18–19 Uhr oft am entspanntesten, " +
                    "wenn das Wetter passt. Tipps für wenig Verkehr?",
            likeCount = 18,
            commentCount = 6,
            isLikedByMe = true,
            tag = PostTag.TOURISTENFAHRTEN
        )

        val userPost2 = CommunityPost(
            id = "post-2",
            author = userCaro,
            createdAt = now.minusDays(1),
            title = "Street/Track-Setup Laguna Coupé",
            content = "Fahre aktuell PS4S auf 18 Zoll, Stahlflex-Leitungen und etwas mehr Sturz vorne. " +
                    "Für TF mega stabil, aber noch daily-tauglich. Was würdet ihr als nächstes machen?",
            likeCount = 32,
            commentCount = 9,
            tag = PostTag.SETUP
        )

        val userPost3 = CommunityPost(
            id = "post-3",
            author = userAnon,
            createdAt = now.minusHours(10),
            title = "Erste Runde Nordschleife – Tipps?",
            content = "Bin nächste Woche das erste Mal am Ring. Welche Basics sollte ich auf jeden Fall beachten, " +
                    "damit ich weder mir noch anderen im Weg stehe?",
            likeCount = 12,
            commentCount = 15,
            tag = PostTag.FRAGEN
        )

        val userPost4 = CommunityPost(
            id = "post-4",
            author = userBen,
            createdAt = now.minusDays(2),
            title = "Spotting-Pics Brünnchen",
            content = "Gestern ein paar schöne Shots am Brünnchen gemacht. Wer erkennt sein Auto?",
            likeCount = 40,
            commentCount = 11,
            tag = PostTag.SPOTTING
        )

        // M2-Post nach oben, dann Rest
        return listOf(postM2, userPost2, userPost4, userPost1, userPost3)
    }
}
