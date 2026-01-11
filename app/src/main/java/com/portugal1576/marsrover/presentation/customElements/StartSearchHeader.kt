package com.portugal1576.marsrover.presentation.customElements

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.portugal1576.marsrover.R
import com.portugal1576.marsrover.ui.theme.MarsRoverTheme
import com.portugal1576.marsrover.ui.theme.PortugalGreenFlag
import com.portugal1576.marsrover.ui.theme.PortugalRedWine

@Composable
fun StartSearchHeader(
    query: String,
    onQueryChange: (String) -> Unit,
    onFilterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        Card(
            border = BorderStroke(2.dp, PortugalRedWine),
            colors = CardDefaults.cardColors(containerColor = PortugalGreenFlag),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    singleLine = true,
                    placeholder = { Text("Search by name") },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.06f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.04f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedPlaceholderColor = Color.White.copy(alpha = 0.6f),
                        unfocusedPlaceholderColor = Color.White.copy(alpha = 0.6f),
                        focusedIndicatorColor = Color.White.copy(alpha = 0.35f),
                        unfocusedIndicatorColor = Color.White.copy(alpha = 0.25f),
                        cursorColor = Color.White
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp, top = 10.dp, bottom = 10.dp)
                )

                IconButton(
                    onClick = onFilterClick,
                    modifier = Modifier.padding(start = 6.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_filter),
                        contentDescription = "Filter",
                        tint = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StartSearchHeaderPreview() {
    MarsRoverTheme {
        StartSearchHeader(
            query = "Rick",
            onQueryChange = {},
            onFilterClick = {}
        )
    }
}
