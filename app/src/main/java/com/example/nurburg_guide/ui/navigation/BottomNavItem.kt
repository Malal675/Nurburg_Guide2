package com.example.nurburg_guide.ui.navigation

// Baustein 2.1: Enum für Tabs



import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.People
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.nurburg_guide.R

enum class BottomNavItem(
    @StringRes val titleRes: Int,
    val icon: ImageVector
) {
    Home(
        titleRes = R.string.nav_home,
        icon = Icons.Filled.Home
    ),
    TrackStatus(
        titleRes = R.string.nav_track_status,
        icon = Icons.Filled.Map
    ),
    Community(
        titleRes = R.string.nav_community,
        icon = Icons.Filled.People
    ),
    Calendar(
        titleRes = R.string.nav_calendar,
        icon = Icons.Filled.CalendarToday
    )
}
