package com.portugal1576.marsrover.presentation.customElements

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.portugal1576.marsrover.presentation.screens.details_screen.EpisodePopupState

@Composable
fun EpisodeInfoDialog(
    state: EpisodePopupState,
    titleFont: FontFamily,
    textFont: FontFamily,
    onDismiss: () -> Unit
) {
    when (state) {
        EpisodePopupState.Hidden -> Unit

        is EpisodePopupState.Error -> {
            AlertDialog(
                onDismissRequest = onDismiss,
                title = { Text(text = "Error", fontFamily = titleFont, color = Color.Red) },
                text = { Text(text = state.message, fontFamily = textFont) },
                confirmButton = { TextButton(onClick = onDismiss) { Text("OK") } }
            )
        }

        is EpisodePopupState.Loaded -> {
            val ep = state.episode
            AlertDialog(
                onDismissRequest = onDismiss,
                title = {
                    Text(
                        text = ep.name,
                        fontFamily = titleFont,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        text = "Code: ${ep.code}\nAir date: ${ep.airDate}\nId: ${ep.id}",
                        fontFamily = textFont
                    )
                },
                confirmButton = { TextButton(onClick = onDismiss) { Text("Close") } }
            )
        }
    }
}
