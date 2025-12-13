package com.example.nurburg_guide.ui.navigation

// Baustein 3.5: MainNavHost – einfacher Switch je nach Tab

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nurburg_guide.ui.features.home.HomeScreen
import com.example.nurburg_guide.ui.features.map.MapScreen
import com.example.nurburg_guide.ui.features.trackstatus.TrackStatusScreen
import com.example.nurburg_guide.ui.features.trackstatus.TrackStatusViewModel
import com.example.nurburg_guide.ui.theme.AppBackground

@Composable
fun MainNavHost(
    modifier: Modifier = Modifier,
    selectedItem: BottomNavItem
) {
    // 👇 gemeinsames ViewModel für Track-Status & Map
    val trackStatusViewModel: TrackStatusViewModel = viewModel()
    val sectorsState by trackStatusViewModel.sectors.collectAsState()

    // ✅ Globaler Hintergrund hinter allen Screens
    AppBackground(modifier = modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            when (selectedItem) {
                BottomNavItem.Explore -> HomeScreen()
                BottomNavItem.TrackStatus -> TrackStatusScreen(
                    viewModel = trackStatusViewModel
                )
                BottomNavItem.Map -> MapScreen(
                    sectorsState = sectorsState
                )
            }
        }
    }
}
