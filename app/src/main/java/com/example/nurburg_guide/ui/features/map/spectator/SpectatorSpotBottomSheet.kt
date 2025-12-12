package com.example.nurburg_guide.ui.features.map.spectator

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpectatorSpotBottomSheet(
    meta: SpectatorSpotMeta,
    suggestedParkingName: String?,
    onNavigateToParking: (() -> Unit)?,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    val imageResId = remember(meta.imageName) {
        meta.imageName
            ?.let { context.resources.getIdentifier(it, "drawable", context.packageName) }
            ?.takeIf { it != 0 }
    }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(Modifier.fillMaxWidth().padding(16.dp)) {

            Text(meta.title, style = MaterialTheme.typography.titleLarge)

            Spacer(Modifier.height(12.dp))

            if (imageResId != null) {
                Card(shape = RoundedCornerShape(16.dp)) {
                    Image(
                        painter = painterResource(imageResId),
                        contentDescription = meta.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    )
                }
                Spacer(Modifier.height(12.dp))
            }

            if (!suggestedParkingName.isNullOrBlank()) {
                Text("Empfohlener Parkplatz: $suggestedParkingName", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(6.dp))
            }

            Text(meta.parkingHint, style = MaterialTheme.typography.bodyMedium)

            Spacer(Modifier.height(16.dp))

            if (onNavigateToParking != null) {
                Button(onClick = onNavigateToParking, modifier = Modifier.fillMaxWidth()) {
                    Text("Route zum Parkplatz")
                }
            } else {
                OutlinedButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                    Text("Schließen")
                }
            }

            Spacer(Modifier.height(10.dp))
        }
    }
}
