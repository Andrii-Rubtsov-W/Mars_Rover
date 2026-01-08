package com.portugal1576.marsrover.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.portugal1576.marsrover.presentation.screens.details_screen.DetailsScreenRoot
import com.portugal1576.marsrover.presentation.screens.favorites_screen.FavoritesScreenRoot
import com.portugal1576.marsrover.presentation.screens.start_screen.StartScreenRoot

@Composable
fun AppNavigationRoot(modifier: Modifier) {
    val navController = rememberNavController()
    AppNavigation(modifier = modifier, navController = navController)
}

@Composable
fun AppNavigation(
    modifier: Modifier,
    navController: NavHostController
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screens.StartScreen
    ) {
        composable<Screens.StartScreen> {
            StartScreenRoot(navController = navController)
        }

        composable<Screens.Details> {
            DetailsScreenRoot()
        }

        composable<Screens.Favorites> {
            FavoritesScreenRoot(navController = navController)
        }
    }
}
