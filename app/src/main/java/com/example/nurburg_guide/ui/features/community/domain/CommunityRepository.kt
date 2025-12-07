package com.example.nurburg_guide.ui.features.community.domain

interface CommunityRepository {
    suspend fun getFeed(): List<CommunityPost>
}
