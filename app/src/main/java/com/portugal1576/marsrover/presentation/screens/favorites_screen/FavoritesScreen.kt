package com.portugal1576.marsrover.presentation.screens.favorites_screen

import android.app.Activity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.portugal1576.marsrover.presentation.navigation.Screens
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavoritesScreenRoot(
    navController: NavController,
) {
    val context = LocalContext.current
    val activity = context as? Activity
    FavoritesScreen(
        navigate = { destinationScreen ->
            navController.navigate(destinationScreen)
        },
        finishActivity = {
            activity?.finish()
        }
    )
}

@Composable
fun FavoritesScreen(
    navigate: (destinationScreen: Screens) -> Unit,
    finishActivity: () -> Unit
) {
    val viewModel: FavoritesScreenViewModel = koinViewModel()
    Text(
        text = "Favorites Screen",
    )
}