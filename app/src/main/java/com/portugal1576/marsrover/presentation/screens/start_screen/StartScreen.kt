package com.portugal1576.marsrover.presentation.screens.start_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.portugal1576.marsrover.R
import com.portugal1576.marsrover.presentation.customElements.Background
import com.portugal1576.marsrover.presentation.customElements.ContainerItem
import com.portugal1576.marsrover.presentation.customElements.StartSearchHeader
import com.portugal1576.marsrover.presentation.customElements.StartSortDialog
import com.portugal1576.marsrover.presentation.customElements.bottomMenu.BottomMenu
import com.portugal1576.marsrover.presentation.navigation.DetailsFrom
import com.portugal1576.marsrover.presentation.navigation.Screens
import com.portugal1576.marsrover.ui.theme.MarsRoverTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun StartScreenRoot(navController: NavController) {
    StartScreen(navigate = { destinationScreen -> navController.navigate(destinationScreen) })
}

@Composable
fun StartScreen(
    navigate: (destinationScreen: Screens) -> Unit
) {
    val viewModel: StartScreenViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.loadInitialIfNeeded()
    }

    StartScreenContent(
        state = state,
        navigate = navigate,
        onLoadNextPage = { viewModel.loadNextPage() },
        onToggleFavorite = { viewModel.toggleFavorite(it) },
        onQueryChange = { viewModel.onQueryChange(it) },
        onApplySort = { viewModel.setSortConfig(it) }
    )
}

@Composable
private fun StartScreenContent(
    state: StartScreenState,
    navigate: (destinationScreen: Screens) -> Unit,
    onLoadNextPage: () -> Unit,
    onToggleFavorite: (com.portugal1576.marsrover.domain.model.Character) -> Unit,
    onQueryChange: (String) -> Unit,
    onApplySort: (SortConfig) -> Unit
) {
    val isPreview = LocalInspectionMode.current
    var showSortDialog by remember { mutableStateOf(false) }

    val currentQuery = (state as? StartScreenState.Loaded)?.query ?: ""
    val currentSort = (state as? StartScreenState.Loaded)?.sort ?: SortConfig()

    if (showSortDialog) {
        StartSortDialog(
            current = currentSort,
            onApply = {
                onApplySort(it)
                showSortDialog = false
            },
            onDismiss = { showSortDialog = false }
        )
    }

    if (isPreview) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF101010))
        ) {
            StartSearchHeader(
                query = currentQuery,
                onQueryChange = {},
                onFilterClick = {},
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
            )

            when (state) {
                is StartScreenState.Loading -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(56.dp))
                    }
                }

                is StartScreenState.Error -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = state.message, color = Color.Red)
                    }
                }

                is StartScreenState.Loaded -> {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
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
            }

            BottomMenu(
                current = Screens.StartScreen,
                onNavigate = navigate
            )
        }
        return
    }

    Scaffold(
        bottomBar = { BottomMenu(current = Screens.StartScreen, onNavigate = navigate) }
    ) { innerPadding ->
        Background(res = R.drawable.font_vert, alpha = 1f) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                StartSearchHeader(
                    query = currentQuery,
                    onQueryChange = onQueryChange,
                    onFilterClick = { showSortDialog = true },
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                )

                when (state) {
                    is StartScreenState.Loading -> {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                strokeWidth = 8.dp,
                                color = Color.Blue,
                                modifier = Modifier.size(80.dp)
                            )
                        }
                    }

                    is StartScreenState.Error -> {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = state.message, color = Color.Red)
                        }
                    }

                    is StartScreenState.Loaded -> {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        ) {
                            items(state.items.size) { index ->
                                if (state.canLoadMore && index == state.items.lastIndex && !state.isLoadingMore) {
                                    LaunchedEffect(state.items.size) { onLoadNextPage() }
                                }

                                val character = state.items[index]
                                ContainerItem(
                                    character = character,
                                    onDetailedDescriptionClick = {
                                        navigate(Screens.Details(id = character.id, from = DetailsFrom.START))
                                    },
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
            onToggleFavorite = {},
            onQueryChange = {},
            onApplySort = {}
        )
    }
}

@Preview(name = "StartScreen - Loaded", showBackground = true)
@Composable
private fun StartScreenPreview_Loaded() {
    MarsRoverTheme {
        StartScreenContent(
            state = StartScreenState.Loaded(
                items = emptyList(),
                canLoadMore = true,
                isLoadingMore = false,
                query = "Rick",
                sort = SortConfig(SortKey.STATUS, true)
            ),
            navigate = {},
            onLoadNextPage = {},
            onToggleFavorite = {},
            onQueryChange = {},
            onApplySort = {}
        )
    }
}

@Preview(name = "StartScreen - Error", showBackground = true)
@Composable
private fun StartScreenPreview_Error() {
    MarsRoverTheme {
        StartScreenContent(
            state = StartScreenState.Error(message = "Something went wrong"),
            navigate = {},
            onLoadNextPage = {},
            onToggleFavorite = {},
            onQueryChange = {},
            onApplySort = {}
        )
    }
}
