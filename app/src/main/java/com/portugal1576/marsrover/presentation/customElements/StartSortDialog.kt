package com.portugal1576.marsrover.presentation.customElements

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.portugal1576.marsrover.presentation.screens.start_screen.SortConfig
import com.portugal1576.marsrover.presentation.screens.start_screen.SortKey
import com.portugal1576.marsrover.ui.theme.MarsRoverTheme
import com.portugal1576.marsrover.ui.theme.PortugalGreenFlag
import com.portugal1576.marsrover.ui.theme.PortugalRedWine

@Composable
fun StartSortDialog(
    current: SortConfig,
    onApply: (SortConfig) -> Unit,
    onDismiss: () -> Unit
) {
    val selectedKey = remember(current.key) { mutableStateOf(current.key) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = PortugalGreenFlag,
            border = BorderStroke(2.dp, PortugalRedWine),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Sorting", color = Color.White)

                Column(modifier = Modifier.padding(top = 12.dp)) {
                    SortRow(
                        title = "Status",
                        selected = selectedKey.value == SortKey.STATUS,
                        onClick = { selectedKey.value = SortKey.STATUS }
                    )
                    SortRow(
                        title = "Species",
                        selected = selectedKey.value == SortKey.SPECIES,
                        onClick = { selectedKey.value = SortKey.SPECIES }
                    )
                    SortRow(
                        title = "Gender",
                        selected = selectedKey.value == SortKey.GENDER,
                        onClick = { selectedKey.value = SortKey.GENDER }
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.textButtonColors(contentColor = PortugalRedWine)
                    ) {
                        Text("Cancel")
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    TextButton(
                        onClick = { onApply(SortConfig(key = selectedKey.value)) },
                        colors = ButtonDefaults.textButtonColors(contentColor = PortugalRedWine)
                    ) {
                        Text("Apply")
                    }
                }
            }
        }
    }
}

@Composable
private fun SortRow(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = PortugalRedWine,
                unselectedColor = Color.White.copy(alpha = 0.65f),
                disabledSelectedColor = PortugalRedWine.copy(alpha = 0.5f),
                disabledUnselectedColor = Color.White.copy(alpha = 0.35f)
            )
        )
        Text(
            text = title,
            color = Color.White,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StartSortDialogPreview() {
    MarsRoverTheme {
        StartSortDialog(
            current = SortConfig(key = SortKey.STATUS),
            onApply = {},
            onDismiss = {}
        )
    }
}
