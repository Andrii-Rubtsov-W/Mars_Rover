package com.portugal1576.marsrover.presentation.screens.list_screen

import android.app.Activity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.portugal1576.marsrover.presentation.navigation.Screens
import org.koin.androidx.compose.koinViewModel

@Composable
fun ListScreenRoot(
    navController: NavController,
) {
    val context = LocalContext.current
    val activity = context as? Activity
    ListScreen(
        navigate = { destinationScreen ->
            navController.navigate(destinationScreen)
        },
        finishActivity = {
            activity?.finish()
        }
    )
}

@Composable
fun ListScreen(
    navigate: (destinationScreen: Screens) -> Unit,
    finishActivity: () -> Unit
) {
    val viewModel: ListScreenViewModel = koinViewModel()
    Text(
        text = "List Screen",
    )
}