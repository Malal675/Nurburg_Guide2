package com.example.nurburg_guide.ui.features.account

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nurburg_guide.ui.theme.AccentGreen

/**
 * Avatar-Icon oben rechts in der App-Bar.
 * Öffnet ein Menü mit:
 *  - Als Gast fortfahren
 *  - Anmelden
 *  - Registrieren
 *  - Einstellungen (Sprache & Benachrichtigungen)
 */
@Composable
fun AccountMenuIcon(
    modifier: Modifier = Modifier,
) {
    var menuExpanded by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    var notificationsEnabled by remember { mutableStateOf(true) }
    val currentLanguage = "Deutsch"       // später aus Settings / Backend holen

    Box(modifier = modifier) {
        IconButton(
            onClick = { menuExpanded = true },
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape),
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Account & Einstellungen",
                tint = AccentGreen,
            )
        }

        DropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false },
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = "Als Gast fortfahren",
                        fontWeight = FontWeight.SemiBold,
                    )
                },
                onClick = {
                    // TODO: falls wir später zwischen Gast / eingeloggtem User unterscheiden
                    menuExpanded = false
                },
            )

            Divider()

            DropdownMenuItem(
                text = { Text("Anmelden") },
                onClick = {
                    // TODO: Login-Screen/Navi aufrufen
                    menuExpanded = false
                },
            )

            DropdownMenuItem(
                text = { Text("Registrieren") },
                onClick = {
                    // TODO: Registrieren-Screen/Navi aufrufen
                    menuExpanded = false
                },
            )

            Divider()

            DropdownMenuItem(
                text = { Text("Einstellungen") },
                onClick = {
                    menuExpanded = false
                    showSettingsDialog = true
                },
            )
        }
    }

    if (showSettingsDialog) {
        AccountSettingsDialog(
            notificationsEnabled = notificationsEnabled,
            onNotificationsChanged = { notificationsEnabled = it },
            currentLanguage = currentLanguage,
            onClose = { showSettingsDialog = false },
        )
    }
}

/**
 * Kleiner Dialog für Sprache & Benachrichtigungen.
 */
@Composable
private fun AccountSettingsDialog(
    notificationsEnabled: Boolean,
    onNotificationsChanged: (Boolean) -> Unit,
    currentLanguage: String,
    onClose: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onClose,
        confirmButton = {
            TextButton(onClick = onClose) {
                Text("Schließen")
            }
        },
        title = { Text(text = "Einstellungen") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                // Sprache
                Column {
                    Text(
                        text = "Sprache",
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = "Aktuell: $currentLanguage",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Text(
                        text = "Weitere Sprachen kommen später.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Notifications
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(
                            text = "Benachrichtigungen",
                            fontWeight = FontWeight.SemiBold,
                        )
                        Text(
                            text = "News & Community-Updates",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        )
                    }

                    Switch(
                        checked = notificationsEnabled,
                        onCheckedChange = onNotificationsChanged,
                    )
                }
            }
        },
    )
}
