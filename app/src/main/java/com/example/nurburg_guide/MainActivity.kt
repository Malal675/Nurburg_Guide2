package com.example.nurburg_guide

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.nurburg_guide.ui.navigation.BottomNavBar
import com.example.nurburg_guide.ui.navigation.BottomNavItem
import com.example.nurburg_guide.ui.navigation.MainNavHost
import com.example.nurburg_guide.ui.theme.NurburgGuideTheme

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isDarkTheme by remember { mutableStateOf(true) }

            NurburgGuideTheme(darkTheme = isDarkTheme) {

                // Start-Tab: Explore
                var selectedItem by remember { mutableStateOf(BottomNavItem.Explore) }

                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = { Text(text = "Nurburg Guide") },
                            actions = {
                                // Theme-Toggle
                                IconButton(onClick = { isDarkTheme = !isDarkTheme }) {
                                    Icon(
                                        imageVector = if (isDarkTheme)
                                            Icons.Filled.LightMode
                                        else
                                            Icons.Filled.DarkMode,
                                        contentDescription = "Theme wechseln"
                                    )
                                }

                                // Avatar-Icon (Account / Einstellungen – später)
                                IconButton(
                                    onClick = {
                                        // TODO: Später Account-/Einstellungs-Screen öffnen
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Person,
                                        contentDescription = "Account & Einstellungen"
                                    )
                                }
                            }
                        )
                    },
                    bottomBar = {
                        BottomNavBar(
                            selectedItem = selectedItem,
                            onItemSelected = { selectedItem = it }
                        )
                    }
                ) { innerPadding ->
                    MainNavHost(
                        modifier = Modifier.padding(innerPadding),
                        selectedItem = selectedItem
                    )
                }
            }
        }
    }
}
