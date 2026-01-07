package com.portugal1576.marsrover.presentation.customElements.bottomMenu

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.portugal1576.marsrover.presentation.navigation.Screens

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

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = false,
                onClick = { onNavigate(item.destination) },
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconID),
                        contentDescription = null
                    )
                },
                label = { Text(text = item.title) }
            )
        }
    }
}
