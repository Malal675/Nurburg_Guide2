package com.example.nurburg_guide.ui.navigation

// Baustein 3.5: MainNavHost – einfacher Switch je nach Tab

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.nurburg_guide.ui.features.calendar.ui.CalendarScreen
import com.example.nurburg_guide.ui.features.community.CommunityScreen
import com.example.nurburg_guide.ui.features.home.HomeScreen
import com.example.nurburg_guide.ui.features.trackstatus.TrackStatusScreen
import com.example.nurburg_guide.ui.features.map.MapScreen


@Composable
fun MainNavHost(
    modifier: Modifier = Modifier,
    selectedItem: BottomNavItem
) {
    Box(modifier = modifier) {
        when (selectedItem) {
            BottomNavItem.Explore -> HomeScreen()
            BottomNavItem.TrackStatus -> TrackStatusScreen()
            BottomNavItem.Community -> CommunityScreen()
            BottomNavItem.Calendar -> CalendarScreen()
            BottomNavItem.Map -> MapScreen()
        }
    }
}
