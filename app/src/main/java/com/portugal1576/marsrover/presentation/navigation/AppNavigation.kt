package com.portugal1576.marsrover.presentation.navigation

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.portugal1576.marsrover.data.model.PlayElement
import com.portugal1576.marsrover.presentation.screens.details_screen.DetailsScreenRoot
import com.portugal1576.marsrover.presentation.screens.favorites_screen.FavoritesScreenRoot
import com.portugal1576.marsrover.presentation.screens.list_screen.ListScreenRoot
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf

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
        startDestination = Screens.ListScreen
    ) {
        composable<Screens.ListScreen> {
            ListScreenRoot(navController = navController)
        }

        composable<Screens.Details>(
            typeMap = mapOf(
                typeOf<Screens.Details>() to parcelableType<Screens.Details>(),
                typeOf<PlayElement>() to parcelableType<PlayElement>()
            )
        ) { backStackEntry ->
            val detail = backStackEntry.toRoute<Screens.Details>()
            DetailsScreenRoot(navController, detail.name)
        }

        composable<Screens.Favorites> {
            FavoritesScreenRoot(navController = navController)
        }
    }
}

inline fun <reified T : Parcelable> parcelableType(
    isNullableAllowed: Boolean = false,
    json: Json = Json,
) = object : NavType<T>(isNullableAllowed = isNullableAllowed) {
    override fun get(bundle: Bundle, key: String) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, T::class.java)
        } else {
            @Suppress("DEPRECATION")
            bundle.getParcelable(key)
        }

    override fun parseValue(value: String): T = json.decodeFromString(value)

    override fun serializeAsValue(value: T): String = json.encodeToString(value)

    override fun put(bundle: Bundle, key: String, value: T) = bundle.putParcelable(key, value)
}
