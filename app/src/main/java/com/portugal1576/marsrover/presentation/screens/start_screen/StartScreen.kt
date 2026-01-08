package com.portugal1576.marsrover.presentation.screens.start_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.portugal1576.marsrover.presentation.customElements.Background
import com.portugal1576.marsrover.presentation.customElements.ContainerItem
import com.portugal1576.marsrover.presentation.customElements.bottomMenu.BottomMenu
import com.portugal1576.marsrover.presentation.navigation.Screens
import com.portugal1576.marsrover.ui.theme.MarsRoverTheme
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun StartScreenRoot(navController: NavController) {
    StartScreen(
        navigate = { destinationScreen -> navController.navigate(destinationScreen) }
    )
}

@Composable
fun StartScreen(
    navigate: (destinationScreen: Screens) -> Unit
) {
    val viewModel: StartScreenViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        delay(500)
        viewModel.refreshCharacters()
    }

    StartScreenContent(
        state = state,
        navigate = navigate,
        onLoadNextPage = { viewModel.loadNextPage() },
        onToggleFavorite = { viewModel.toggleFavorite(it) }
    )
}

@Composable
private fun StartScreenContent(
    state: StartScreenState,
    navigate: (destinationScreen: Screens) -> Unit,
    onLoadNextPage: () -> Unit,
    onToggleFavorite: (com.portugal1576.marsrover.domain.model.Character) -> Unit
) {
    val isPreview = LocalInspectionMode.current

    if (isPreview) {
        Scaffold(
            bottomBar = {
                BottomMenu(
                    current = Screens.StartScreen,
                    onNavigate = navigate
                )
            }
        ) { innerPadding ->
            when (state) {
                is StartScreenState.Loading -> {
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

                is StartScreenState.Loaded -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF101010))
                            .padding(innerPadding)
                    ) {
                        val count = state.items.size.coerceAtLeast(6)
                        items(count) { idx ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                                    .background(Color(0xFF1B1B1B))
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = state.items.getOrNull(idx)?.name ?: "Item ${idx + 1}",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                is StartScreenState.Error -> {
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

    Background(alpha = 1f) {
        Scaffold(
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets(0),
            bottomBar = {
                BottomMenu(
                    current = Screens.StartScreen,
                    onNavigate = navigate
                )
            }
        ) { innerPadding ->
            when (state) {
                is StartScreenState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .windowInsetsPadding(WindowInsets.statusBars),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            strokeWidth = 8.dp,
                            color = Color.Blue,
                            modifier = Modifier.size(80.dp)
                        )
                    }
                }

                is StartScreenState.Loaded -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .windowInsetsPadding(WindowInsets.statusBars)
                    ) {
                        items(state.items.size) { index ->
                            if (state.canLoadMore && index == state.items.lastIndex && !state.isLoadingMore) {
                                LaunchedEffect(state.items.size) { onLoadNextPage() }
                            }

                            val character = state.items[index]
                            ContainerItem(
                                character = character,
                                onDetailedDescriptionClick = { navigate(Screens.Details(character.id)) },
                                onFavoriteClick = { onToggleFavorite(character) }
                            )
                        }

                        if (state.isLoadingMore) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                    }
                }

                is StartScreenState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .windowInsetsPadding(WindowInsets.statusBars),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = state.message, color = Color.Red)
                    }
                }
            }
        }
    }
}

@Preview(name = "StartScreen - Loading", showBackground = true)
@Composable
private fun StartScreenPreview_Loading() {
    MarsRoverTheme {
        StartScreenContent(
            state = StartScreenState.Loading,
            navigate = {},
            onLoadNextPage = {},
            onToggleFavorite = {}
        )
    }
}
