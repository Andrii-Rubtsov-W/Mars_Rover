package com.portugal1576.marsrover.presentation.customElements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.portugal1576.marsrover.R

@Composable
internal fun Background(
    modifier: Modifier = Modifier,
    res: Int,
    alpha: Float,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Image(
            modifier = modifier.fillMaxSize(),
            painter = painterResource(res),
            contentScale = ContentScale.Crop,
            contentDescription = "Background Image",
            alpha = alpha
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBackground() {
    Background(
        res = R.drawable.font_vert,
        alpha = 1f
    ) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Hello, Wine Portugal!",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
        }
    }
}
