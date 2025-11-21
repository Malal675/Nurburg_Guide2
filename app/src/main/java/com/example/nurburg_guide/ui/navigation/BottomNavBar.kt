package com.example.nurburg_guide.ui.navigation

// Baustein 2.3: Untere Navigationsleiste

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.nurburg_guide.ui.theme.AccentGreen

@Composable
fun BottomNavBar(
    selectedItem: BottomNavItem,
    onItemSelected: (BottomNavItem) -> Unit
) {
    NavigationBar {
        BottomNavItem.values().forEach { item ->
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
                    // alles andere (unselected) = Standardfarben von Material3
                    indicatorColor = Color.Transparent // kein grüner Bubble-Hintergrund
                )
            )
        }
    }
}
