package com.portugal1576.marsrover.presentation.customElements

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.portugal1576.marsrover.ui.theme.PortugalGreenFlag

@Composable
fun RemoveFavoriteDialog(
    name: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    titleFont: FontFamily? = null,
    textFont: FontFamily? = null
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Remove from favorites?",
                color = Color.White,
                fontFamily = titleFont,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = "Do you really want to remove \"$name\" from favorites?",
                color = Color.White,
                fontFamily = textFont
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = "Remove", color = Color.Red)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancel", color = Color.White)
            }
        },
        containerColor = PortugalGreenFlag
    )
}
