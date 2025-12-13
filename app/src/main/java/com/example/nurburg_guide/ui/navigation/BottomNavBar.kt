package com.example.nurburg_guide.ui.navigation

// Baustein 2.3: Untere Navigationsleiste

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.nurburg_guide.ui.theme.AccentGreen
import androidx.compose.ui.unit.dp

@Composable
fun BottomNavBar(
    selectedItem: BottomNavItem,
    onItemSelected: (BottomNavItem) -> Unit
) {
    val orderedItems = BottomNavItem.values()
        .sortedBy { item ->
            when (item.name.lowercase()) {
                "map", "karte" -> 0
                "explore" -> 1
                "trackstatus", "track_status", "status" -> 2
                else -> 99
            }
        }

    NavigationBar(
        // ✅ leicht transparent, damit Background durchscheint – trotzdem gut lesbar
        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.86f),
        tonalElevation = 0.dp // optional: weniger “grauer Block”
    ) {
        orderedItems.forEach { item ->
            val isSelected = item == selectedItem

            NavigationBarItem(
                selected = isSelected,
                onClick = { onItemSelected(item) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = stringResource(id = item.titleRes)
                    )
                },
                label = {
                    Text(text = stringResource(id = item.titleRes))
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AccentGreen,
                    selectedTextColor = AccentGreen,
                    indicatorColor = Color.Transparent // kein Bubble-Hintergrund
                )
            )
        }
    }
}
