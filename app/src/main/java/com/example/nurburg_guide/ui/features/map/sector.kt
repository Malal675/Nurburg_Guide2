package com.example.nurburg_guide.ui.features.map

import com.google.android.gms.maps.model.LatLng

data class Sector(
    val id: Int,
    val name: String,
    val points: List<LatLng>
)