package com.example.nurburg_guide.ui.features.map.model

data class GuideLocation(
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val category: LocationCategory,
    val url: String? = null
)