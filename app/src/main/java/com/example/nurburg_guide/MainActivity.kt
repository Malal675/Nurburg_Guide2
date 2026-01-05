package com.example.nurburg_guide

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.nurburg_guide.ui.features.account.AccountMenuIcon
import com.example.nurburg_guide.ui.navigation.BottomNavBar
import com.example.nurburg_guide.ui.navigation.BottomNavItem
import com.example.nurburg_guide.ui.navigation.MainNavHost
import com.example.nurburg_guide.ui.theme.NurburgGuideTheme

private const val RACETAXI_M3_URL =
    "https://www.getspeed-racetaxi.de/bmw-m3-competition-touring?_gl=1*bhykaq*_up*MQ..*_gs*MQ..&gclid=CjwKCAiA3-3KBhBiEiwA2x7FdIFwdj2dYuPR6lN_P7oCaAPIqSxxoB-vNB_gg4OXlAHwoqmAQSiiLRoC_wQQAvD_BwE&gbraid=0AAAAAqcBHeYBmH0m-qldOVZxvC6Tchnkk"

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var isDarkTheme by remember { mutableStateOf(true) }

            NurburgGuideTheme(darkTheme = isDarkTheme) {
                // Start-Tab: Explore
                var selectedItem by remember { mutableStateOf(BottomNavItem.Explore) }

                // NEU: RaceTaxi-Popup beim Start
                var showRaceTaxiDialog by remember { mutableStateOf(true) }

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

                        // NEU: RaceTaxi-Werbung
                        if (showRaceTaxiDialog) {
                            RaceTaxiPromoDialog(
                                onClose = { showRaceTaxiDialog = false }
                            )
                        }

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

/**
 * NEU: RaceTaxi-Popup mit Bild + Button
 */
@Composable
private fun RaceTaxiPromoDialog(
    onClose: () -> Unit,
) {
    val context = LocalContext.current

    Dialog(onDismissRequest = onClose) {
        Card(
            shape = MaterialTheme.shapes.large,
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Column {
                Box {
                    Image(
                        painter = painterResource(id = R.drawable.racetaxi_promo),
                        contentDescription = "RaceTaxi am Nürburgring",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp),
                        contentScale = ContentScale.Crop
                    )

                    IconButton(
                        onClick = onClose,
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Werbung schließen"
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Erlebe den Ring als Beifahrer",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "BMW M3 Competition Touring – Racetaxi-Laps mit Profi-Fahrern. " +
                                "Perfekt, um Linien und Bremspunkte kennenzulernen.",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            openUrl(context, RACETAXI_M3_URL)
                            onClose()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Zur Seite")
                    }
                }
            }
        }
    }
}

private fun openUrl(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}
