package com.example.nurburg_guide.ui.features.community.domain

import java.time.LocalDateTime

data class UserProfile(
    val id: String,
    val displayName: String,
    val avatarInitials: String
)

enum class PostTag {
    TOURISTENFAHRTEN,
    SETUP,
    SPOTTING,
    FRAGEN,
    GENERAL
}

data class CommunityComment(
    val id: String,
    val author: UserProfile,
    val createdAt: LocalDateTime,
    val content: String
)

data class CommunityPost(
    val id: String,
    val author: UserProfile,
    val createdAt: LocalDateTime,
    val title: String?,
    val content: String,
    val imageUrl: String? = null,          // später: echte Bilder (Coil / Upload)
    val likeCount: Int = 0,
    val commentCount: Int = 0,
    val isLikedByMe: Boolean = false,
    val tag: PostTag = PostTag.GENERAL,
    val comments: List<CommunityComment> = emptyList(),

    // NEU: Art des Beitrags (Frage / Umfrage / normaler Post)
    val type: PostType = PostType.NORMAL,

    // NEU: Nürburgring-/Renn-spezifische Kategorie
    val category: PostCategory = PostCategory.GENERAL
)
