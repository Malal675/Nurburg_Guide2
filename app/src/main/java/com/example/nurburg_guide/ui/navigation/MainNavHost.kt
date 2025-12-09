package com.example.nurburg_guide.ui.navigation

// Baustein 3.5: MainNavHost – einfacher Switch je nach Tab

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.nurburg_guide.ui.features.calendar.CalendarScreen
import com.example.nurburg_guide.ui.features.community.CommunityScreen
import com.example.nurburg_guide.ui.features.home.HomeScreen
import com.example.nurburg_guide.ui.features.map.MapScreen
import com.example.nurburg_guide.ui.features.trackstatus.TrackStatusScreen
import com.example.nurburg_guide.ui.features.trackstatus.SectorState
import com.example.nurburg_guide.ui.features.trackstatus.SectorStatus

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
            BottomNavItem.Map -> {
                // 👉 vorerst Dummy-Zustände für alle Sektoren der Nordschleife
                val sectorsState = listOf(
                    SectorState(1, SectorStatus.GREEN),    // Tiergarten
                    SectorState(2, SectorStatus.YELLOW),   // T13
                    SectorState(3, SectorStatus.GREEN),    // Hatzenbach
                    SectorState(4, SectorStatus.YELLOW),   // Quiddelbacher Höhe
                    SectorState(5, SectorStatus.GREEN),    // Flugplatz
                    SectorState(6, SectorStatus.RED),      // Schwedenkreuz
                    SectorState(7, SectorStatus.YELLOW),   // Aremberg
                    SectorState(8, SectorStatus.GREEN),    // Fuchsröhre
                    SectorState(9, SectorStatus.RED),      // Adenauer Forst
                    SectorState(10, SectorStatus.GREEN),   // Metzgesfeld
                    SectorState(11, SectorStatus.YELLOW),  // Kallenhard
                    SectorState(12, SectorStatus.GREEN),   // Wehrseifen
                    SectorState(13, SectorStatus.RED),     // Breidscheid
                    SectorState(14, SectorStatus.YELLOW),  // Exmühle
                    SectorState(15, SectorStatus.GREEN),   // Lauda-Links
                    SectorState(16, SectorStatus.GREEN),   // Bergwerk
                    SectorState(17, SectorStatus.YELLOW),  // Kesselchen
                    SectorState(18, SectorStatus.GREEN),   // Klostertal
                    SectorState(19, SectorStatus.RED),     // Caracciola-Karussell
                    SectorState(20, SectorStatus.YELLOW),  // Hohe Acht
                    SectorState(21, SectorStatus.GREEN),   // Hedwigshöhe
                    SectorState(22, SectorStatus.RED),     // Wippermann
                    SectorState(23, SectorStatus.GREEN),   // Eschbach
                    SectorState(24, SectorStatus.GREEN),   // Brünnchen
                    SectorState(25, SectorStatus.YELLOW),  // Eiskurve
                    SectorState(26, SectorStatus.GREEN),   // Pflanzgarten I
                    SectorState(27, SectorStatus.YELLOW),  // Pflanzgarten II
                    SectorState(28, SectorStatus.RED),     // Stefan-Bellof-S
                    SectorState(29, SectorStatus.GREEN),   // Schwalbenschwanz
                    SectorState(30, SectorStatus.YELLOW),  // Kleines Karussell
                    SectorState(31, SectorStatus.GREEN),   // Galgenkopf
                    SectorState(32, SectorStatus.GREEN),   // Döttinger Höhe
                )

                MapScreen(sectorsState = sectorsState)
            }
        }
    }
}