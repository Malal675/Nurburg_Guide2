package com.example.nurburg_guide.ui.navigation

// Baustein 2.3: Untere Navigationsleiste



import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

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
                }
            )
        }
    }
}
