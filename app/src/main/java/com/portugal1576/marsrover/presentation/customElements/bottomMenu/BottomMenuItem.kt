package com.portugal1576.marsrover.presentation.customElements.bottomMenu

import com.portugal1576.marsrover.R
import com.portugal1576.marsrover.presentation.navigation.Screens

sealed class BottomMenuItem(
    val title: String,
    val iconID: Int,
    val destination: Screens
) {
    data object Home : BottomMenuItem("Home", R.drawable.ic_home, Screens.StartScreen)
    data object Favorites : BottomMenuItem("Favorites", R.drawable.ic_favorite, Screens.Favorites)
}
