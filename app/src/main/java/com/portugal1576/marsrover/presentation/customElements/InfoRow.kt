package com.portugal1576.marsrover.presentation.customElements

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.portugal1576.marsrover.R
import com.portugal1576.marsrover.ui.theme.MarsRoverTheme
import com.portugal1576.marsrover.ui.theme.PortugalYellow

@Composable
fun InfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$label:",
            fontSize = 12.sp,
            fontFamily = FontFamily(Font(R.font.serif_bold_italic, FontWeight.Normal)),
            color = PortugalYellow,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value.ifBlank { "-" },
            fontSize = 12.sp,
            fontFamily = FontFamily(Font(R.font.robo_medium_font, FontWeight.Normal)),
            color = Color.White,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun InfoRowPreview() {
    MarsRoverTheme {
        InfoRow(
            label = "Origin",
            value = "Earth (C-137)"
        )
    }
}