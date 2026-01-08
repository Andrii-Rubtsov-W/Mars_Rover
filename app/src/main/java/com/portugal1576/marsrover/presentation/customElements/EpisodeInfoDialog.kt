package com.portugal1576.marsrover.presentation.customElements

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.portugal1576.marsrover.R
import com.portugal1576.marsrover.presentation.screens.details_screen.EpisodePopupState
import com.portugal1576.marsrover.ui.theme.MarsRoverTheme

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
                text = { Text(
                    text = state.message,
                    fontFamily = FontFamily(Font(R.font.serif_bold_italic, FontWeight.Bold))
                ) },
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
                        fontFamily = FontFamily(Font(R.font.serif_bold_italic, FontWeight.Bold)),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        text = "Code: ${ep.code}\nAir date: ${ep.airDate}\nId: ${ep.id}",
                        fontFamily = FontFamily(Font(R.font.serif_bold_italic, FontWeight.Bold)),
                    )
                },
                confirmButton = { TextButton(onClick = onDismiss) { Text("Close") } }
            )
        }
    }
}

@Preview(name = "EpisodeInfoDialog - Error", showBackground = true)
@Composable
private fun EpisodeInfoDialogPreview_Error() {
    MarsRoverTheme {
        EpisodeInfoDialog(
            state = EpisodePopupState.Error("Invalid episode id"),
            titleFont = FontFamily.Default,
            textFont = FontFamily.Default,
            onDismiss = {}
        )
    }
}

@Preview(name = "EpisodeInfoDialog - Loaded", showBackground = true)
@Composable
private fun EpisodeInfoDialogPreview_Loaded() {
    MarsRoverTheme {
        EpisodeInfoDialog(
            state = EpisodePopupState.Loaded(
                episode = com.portugal1576.marsrover.domain.model.Episode(
                    id = 1,
                    name = "Pilot",
                    airDate = "December 2, 2013",
                    code = "S01E01"
                )
            ),
            titleFont = FontFamily.Default,
            textFont = FontFamily.Default,
            onDismiss = {}
        )
    }
}

