package com.portugal1576.marsrover.presentation.customElements

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import com.portugal1576.marsrover.R

@Composable
fun Background(
    @DrawableRes res: Int = R.drawable.font_vert,
    alpha: Float = 1f,
    content: @Composable () -> Unit
) {
    val orientation = LocalConfiguration.current.orientation
    val bgRes = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
        R.drawable.font_hor
    } else {
        res
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
        ) {
            Image(
                painter = painterResource(id = bgRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(alpha = alpha),
                contentScale = ContentScale.Crop
            )
        }
        content()
    }
}
