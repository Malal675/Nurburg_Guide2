package com.example.nurburg_guide

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.nurburg_guide.ui.features.account.AccountMenuIcon
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

                // --- First-launch-Lizenz / Disclaimer ---
                val context = LocalContext.current
                val prefs = remember {
                    context.getSharedPreferences(
                        "nurburg_guide_prefs",
                        Context.MODE_PRIVATE
                    )
                }
                var showLicenseDialog by remember {
                    mutableStateOf(
                        !prefs.getBoolean("license_accepted", false)
                    )
                }
                // -----------------------------------------

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
                                        contentDescription = "Theme wechseln",
                                    )
                                }

                                // Globales Account-Menü (Avatar)
                                AccountMenuIcon()
                            },
                        )
                    },
                    bottomBar = {
                        BottomNavBar(
                            selectedItem = selectedItem,
                            onItemSelected = { selectedItem = it },
                        )
                    },
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        MainNavHost(
                            modifier = Modifier,
                            selectedItem = selectedItem,
                        )

                        // Lizenz-Hinweis nur anzeigen, wenn nötig
                        if (showLicenseDialog) {
                            LicenseDisclaimerDialog(
                                onAccept = {
                                    prefs.edit()
                                        .putBoolean("license_accepted", true)
                                        .apply()
                                    showLicenseDialog = false
                                },
                                onDismiss = {
                                    // Optional: App trotzdem nutzbar lassen
                                    showLicenseDialog = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Hinweis: Inoffizielle App, keine Verbindung zur Nürburgring 1927 GmbH & Co. KG.
 * Wird standardmäßig nur beim ersten Start angezeigt.
 */
@Composable
private fun LicenseDisclaimerDialog(
    onAccept: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Wichtiger Hinweis")
        },
        text = {
            Text(
                text = "Diese App ist ein inoffizieller Fan-Guide und steht in keiner " +
                        "geschäftlichen Verbindung zur Nürburgring 1927 GmbH & Co. KG " +
                        "oder deren Partnern.\n\n" +
                        "Alle Angaben zu Zeiten, Events, Wetter usw. sind ohne Gewähr " +
                        "und können von den offiziellen Informationen abweichen. " +
                        "Verbindliche Infos findest du immer auf den offiziellen " +
                        "Kanälen des Nürburgrings.\n\n" +
                        "Durch Fortfahren bestätigst du, dass du diesen Hinweis " +
                        "verstanden hast."
            )
        },
        confirmButton = {
            TextButton(onClick = onAccept) {
                Text(text = "Verstanden")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Später")
            }
        }
    )
}
