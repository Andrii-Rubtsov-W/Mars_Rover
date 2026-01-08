package com.portugal1576.marsrover.presentation.screens.favorites_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.portugal1576.marsrover.R
import com.portugal1576.marsrover.presentation.customElements.Background
import com.portugal1576.marsrover.presentation.customElements.ContainerItem
import com.portugal1576.marsrover.presentation.customElements.bottomMenu.BottomMenu
import com.portugal1576.marsrover.presentation.navigation.DetailsFrom
import com.portugal1576.marsrover.presentation.navigation.Screens
import com.portugal1576.marsrover.ui.theme.MarsRoverTheme
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavoritesScreenRoot(navController: NavController) {
    FavoritesScreen(
        navigate = { destinationScreen -> navController.navigate(destinationScreen) }
    )
}

@Composable
fun FavoritesScreen(
    navigate: (destinationScreen: Screens) -> Unit
) {
    val viewModel: FavoritesScreenViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        delay(250)
        viewModel.refreshPlayers()
    }

    FavoritesScreenContent(
        state = state,
        navigate = navigate
    )
}

@Composable
private fun FavoritesScreenContent(
    state: FavoritesScreenState,
    navigate: (destinationScreen: Screens) -> Unit
) {
    val isPreview = LocalInspectionMode.current

    if (isPreview) {
        Scaffold(
            bottomBar = {
                BottomMenu(
                    current = Screens.Favorites,
                    onNavigate = navigate
                )
            }
        ) { innerPadding ->
            when (state) {
                is FavoritesScreenState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF101010))
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(56.dp))
                    }
                }

                is FavoritesScreenState.Loaded -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF101010))
                            .padding(innerPadding)
                    ) {
                        items(state.character.size) { idx ->
                            val ch = state.character[idx]
                            Text(
                                text = ch.name,
                                color = Color.White,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }

                is FavoritesScreenState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF101010))
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = state.message, color = Color.Red)
                    }
                }
            }
        }
        return
    }

    Scaffold(
        bottomBar = {
            BottomMenu(
                current = Screens.Favorites,
                onNavigate = navigate
            )
        }
    ) { innerPadding ->
        when (state) {
            is FavoritesScreenState.Loading -> {
                Background(res = R.drawable.font_vert, alpha = 1f) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            strokeWidth = 8.dp,
                            color = Color.Blue,
                            modifier = Modifier.size(80.dp)
                        )
                    }
                }
            }

            is FavoritesScreenState.Loaded -> {
                Background(res = R.drawable.font_vert, alpha = 1f) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        items(state.character.size) { index ->
                            val character = state.character[index]
                            ContainerItem(
                                character = character,
                                onDetailedDescriptionClick = {
                                    navigate(
                                        Screens.Details(
                                            id = character.id,
                                            from = DetailsFrom.FAVORITES
                                        )
                                    )
                                },
                                onFavoriteClick = {}
                            )
                        }
                    }
                }
            }

            is FavoritesScreenState.Error -> {
                Background(res = R.drawable.font_vert, alpha = 1f) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = state.message, color = Color.Red)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoritesPreview() {
    MarsRoverTheme {
        FavoritesScreenContent(
            state = FavoritesScreenState.Error("Favorite list is empty"),
            navigate = {}
        )
    }
}
