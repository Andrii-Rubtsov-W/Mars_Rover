package com.portugal1576.marsrover.presentation.screens.details_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.portugal1576.marsrover.R
import com.portugal1576.marsrover.domain.model.Character
import com.portugal1576.marsrover.presentation.customElements.Background
import com.portugal1576.marsrover.presentation.customElements.InfoRow
import com.portugal1576.marsrover.ui.theme.MarsRoverTheme
import com.portugal1576.marsrover.ui.theme.PortugalGreenFlag
import com.portugal1576.marsrover.ui.theme.PortugalRedWine
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailsScreenRoot() {
    val viewModel: DetailsScreenViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    DetailsScreen(
        state = state,
        onFavoriteClick = { viewModel.toggleFavorite() }
    )
}

@Composable
fun DetailsScreen(
    state: DetailsScreenState,
    onFavoriteClick: () -> Unit
) {
    val isPreview = LocalInspectionMode.current

    if (isPreview) {
        Box(modifier = Modifier.fillMaxSize()) {
            DetailsScreenContent(
                state = state,
                onFavoriteClick = onFavoriteClick
            )
        }
        return
    }

    Background(res = R.drawable.font_vert, alpha = 1f) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
        ) {
            DetailsScreenContent(
                state = state,
                onFavoriteClick = onFavoriteClick
            )
        }
    }
}

@Composable
private fun DetailsScreenContent(
    state: DetailsScreenState,
    onFavoriteClick: () -> Unit
) {
    when (state) {
        is DetailsScreenState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    strokeWidth = 8.dp,
                    color = Color.Blue,
                    modifier = Modifier.size(80.dp)
                )
            }
        }

        is DetailsScreenState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = state.message, color = Color.Red)
            }
        }

        is DetailsScreenState.Loaded -> {
            val character = state.character
            val scrollState = rememberScrollState()

            val nameFont = FontFamily(Font(R.font.inter_regular, FontWeight.Normal))
            val statusFont = FontFamily(Font(R.font.robo_thin_font, FontWeight.Normal))

            Card(
                border = BorderStroke(2.dp, PortugalRedWine),
                colors = CardDefaults.cardColors(containerColor = PortugalGreenFlag),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 6.dp, horizontal = 12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Card(
                            border = BorderStroke(2.dp, PortugalRedWine),
                            colors = CardDefaults.cardColors(containerColor = PortugalGreenFlag),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.size(110.dp)
                        ) {
                            AsyncImage(
                                model = character.image,
                                contentDescription = character.name,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Column(
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .weight(1f)
                                .fillMaxHeight(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = character.name.ifBlank { "No name available" },
                                color = Color.White,
                                fontFamily = nameFont,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = character.status.ifBlank { "Unknown status" },
                                fontSize = 14.sp,
                                color = Color.White,
                                fontFamily = statusFont,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Image(
                            painter = painterResource(id = R.drawable.ic_favorite),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(if (character.isFavorite) Color.Red else Color.White),
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .clickable { onFavoriteClick() }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    InfoRow(label = "Species", value = character.species)
                    InfoRow(label = "Gender", value = character.gender)
                    InfoRow(label = "Origin", value = character.originName)
                    InfoRow(label = "Location", value = character.locationName)

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Episodes",
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.robo_medium_font, FontWeight.Normal)),
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    if (character.episodeUrls.isEmpty()) {
                        Text(
                            text = "No episodes",
                            fontSize = 14.sp,
                            color = Color.White,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        val episodesBoxScroll = rememberScrollState()
                        val maxVisible = 5
                        val itemHeight = 26.dp
                        val containerHeight = (itemHeight * minOf(character.episodeUrls.size, maxVisible))
                            .coerceAtLeast(78.dp)

                        Card(
                            border = BorderStroke(2.dp, PortugalRedWine),
                            colors = CardDefaults.cardColors(containerColor = PortugalGreenFlag),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(containerHeight)
                                .padding(top = 6.dp, bottom = 10.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(episodesBoxScroll)
                                    .padding(horizontal = 12.dp, vertical = 10.dp)
                            ) {
                                character.episodeUrls.forEach { url ->
                                    val ep = url.substringAfterLast("/", missingDelimiterValue = url)
                                    Text(
                                        text = "Episode $ep",
                                        fontSize = 14.sp,
                                        color = Color.White,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(name = "DetailsScreen - Loading", showBackground = true)
@Composable
private fun DetailsScreenPreviewLoading() {
    MarsRoverTheme {
        DetailsScreenContent(
            state = DetailsScreenState.Loading,
            onFavoriteClick = {}
        )
    }
}

@Preview(name = "DetailsScreen - Loaded", showBackground = true)
@Composable
private fun DetailsScreenPreviewLoaded() {
    MarsRoverTheme {
        DetailsScreenContent(
            state = DetailsScreenState.Loaded(
                character = Character(
                    id = 1,
                    name = "Rick Sanchez",
                    image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                    status = "Alive",
                    species = "Human",
                    gender = "Male",
                    originName = "Earth (C-137)",
                    locationName = "Citadel of Ricks",
                    episodeUrls = listOf(
                        "https://rickandmortyapi.com/api/episode/1",
                        "https://rickandmortyapi.com/api/episode/2",
                        "https://rickandmortyapi.com/api/episode/3",
                        "https://rickandmortyapi.com/api/episode/4",
                        "https://rickandmortyapi.com/api/episode/5",
                        "https://rickandmortyapi.com/api/episode/6",
                        "https://rickandmortyapi.com/api/episode/7"
                    ),
                    isFavorite = true
                )
            ),
            onFavoriteClick = {}
        )
    }
}

@Preview(name = "DetailsScreen - Error", showBackground = true)
@Composable
private fun DetailsScreenPreviewError() {
    MarsRoverTheme {
        DetailsScreenContent(
            state = DetailsScreenState.Error("Load error"),
            onFavoriteClick = {}
        )
    }
}
