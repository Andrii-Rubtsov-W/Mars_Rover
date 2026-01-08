package com.portugal1576.marsrover.presentation.customElements.bottomMenu

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.portugal1576.marsrover.presentation.navigation.Screens
import com.portugal1576.marsrover.ui.theme.PortugalGreenL
import com.portugal1576.marsrover.ui.theme.PortugalRedFlag

@Composable
fun BottomMenu(
    current: Screens,
    onNavigate: (Screens) -> Unit
) {
    val items = when (current) {
        is Screens.StartScreen -> listOf(BottomMenuItem.Favorites)
        is Screens.Favorites -> listOf(BottomMenuItem.Home)
        else -> emptyList()
    }

    val shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)

    Box(modifier = Modifier.navigationBarsPadding()) {
        Surface(
            shape = shape,
            color = PortugalGreenL
        ) {
            Box(
                modifier = Modifier
                    .clip(shape)
                    .fillMaxWidth()
            ) {
                NavigationBar(
                    modifier = Modifier.height(62.dp),
                    containerColor = Color.Transparent,
                    windowInsets = WindowInsets(0)
                ) {
                    items.forEach { item ->
                        val tint =
                            if (item.destination is Screens.Favorites) PortugalRedFlag else Color.White

                        NavigationBarItem(
                            selected = false,
                            onClick = { onNavigate(item.destination) },
                            icon = {
                                Icon(
                                    painter = painterResource(id = item.iconID),
                                    contentDescription = null,
                                    tint = tint
                                )
                            },
                            label = {
                                Text(
                                    text = item.title,
                                    color = tint
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = Color.Transparent,
                                selectedIconColor = tint,
                                selectedTextColor = tint,
                                unselectedIconColor = tint,
                                unselectedTextColor = tint,
                                disabledIconColor = tint.copy(alpha = 0.4f),
                                disabledTextColor = tint.copy(alpha = 0.4f)
                            )
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomMenuPreview_Start() {
    BottomMenu(current = Screens.StartScreen, onNavigate = {})
}

@Preview(showBackground = true)
@Composable
private fun BottomMenuPreview_Favorites() {
    BottomMenu(current = Screens.Favorites, onNavigate = {})
}
