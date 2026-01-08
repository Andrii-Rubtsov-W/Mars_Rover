package com.portugal1576.marsrover.presentation.screens.details_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.unit.times
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.portugal1576.marsrover.R
import com.portugal1576.marsrover.domain.model.Character
import com.portugal1576.marsrover.presentation.customElements.Background
import com.portugal1576.marsrover.presentation.customElements.EpisodeInfoDialog
import com.portugal1576.marsrover.presentation.customElements.InfoRow
import com.portugal1576.marsrover.presentation.customElements.RemoveFavoriteDialog
import com.portugal1576.marsrover.presentation.navigation.DetailsFrom
import com.portugal1576.marsrover.presentation.navigation.Screens
import com.portugal1576.marsrover.ui.theme.MarsRoverTheme
import com.portugal1576.marsrover.ui.theme.PortugalGreenFlag
import com.portugal1576.marsrover.ui.theme.PortugalRedWine
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailsScreenRoot(
    navController: NavHostController,
    from: DetailsFrom
) {
    val viewModel: DetailsScreenViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()
    val episodePopup by viewModel.episodePopup.collectAsState()

    DetailsScreen(
        state = state,
        episodePopup = episodePopup,
        onEpisodeClick = { url -> viewModel.openEpisode(url) },
        onEpisodeDismiss = { viewModel.dismissEpisodePopup() },
        onRemoveConfirmed = {
            viewModel.toggleFavorite()
            val popped = navController.popBackStack()
            if (!popped) {
                navController.navigate(
                    if (from == DetailsFrom.FAVORITES) Screens.Favorites else Screens.StartScreen
                )
            }
        },
        onAddFavorite = { viewModel.toggleFavorite() }
    )
}

@Composable
fun DetailsScreen(
    state: DetailsScreenState,
    episodePopup: EpisodePopupState,
    onEpisodeClick: (String) -> Unit,
    onEpisodeDismiss: () -> Unit,
    onRemoveConfirmed: () -> Unit,
    onAddFavorite: () -> Unit
) {
    val isPreview = LocalInspectionMode.current

    if (isPreview) {
        Box(modifier = Modifier.fillMaxSize()) {
            DetailsScreenContent(
                state = state,
                episodePopup = episodePopup,
                onEpisodeClick = onEpisodeClick,
                onEpisodeDismiss = onEpisodeDismiss,
                onRemoveConfirmed = onRemoveConfirmed,
                onAddFavorite = onAddFavorite
            )
        }
        return
    }

    Scaffold { innerPadding ->
        Background(res = R.drawable.font_vert, alpha = 1f) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                DetailsScreenContent(
                    state = state,
                    episodePopup = episodePopup,
                    onEpisodeClick = onEpisodeClick,
                    onEpisodeDismiss = onEpisodeDismiss,
                    onRemoveConfirmed = onRemoveConfirmed,
                    onAddFavorite = onAddFavorite
                )
            }
        }
    }
}

@Composable
private fun DetailsScreenContent(
    state: DetailsScreenState,
    episodePopup: EpisodePopupState,
    onEpisodeClick: (String) -> Unit,
    onEpisodeDismiss: () -> Unit,
    onRemoveConfirmed: () -> Unit,
    onAddFavorite: () -> Unit
) {
    val isPreview = LocalInspectionMode.current
    var showRemoveDialog by remember { mutableStateOf(false) }

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

            val nameFont =
                if (isPreview) FontFamily.Default else FontFamily(
                    Font(
                        R.font.inter_regular,
                        FontWeight.Normal
                    )
                )
            val statusFont =
                if (isPreview) FontFamily.Default else FontFamily(
                    Font(
                        R.font.robo_thin_font,
                        FontWeight.Normal
                    )
                )
            val episodesTitleFont =
                if (isPreview) FontFamily.Default else FontFamily(
                    Font(
                        R.font.robo_medium_font,
                        FontWeight.Normal
                    )
                )

            if (episodePopup !is EpisodePopupState.Hidden) {
                EpisodeInfoDialog(
                    state = episodePopup,
                    titleFont = nameFont,
                    textFont = statusFont,
                    onDismiss = onEpisodeDismiss
                )
            }

            if (showRemoveDialog) {
                RemoveFavoriteDialog(
                    name = character.name,
                    titleFont = nameFont,
                    textFont = statusFont,
                    onConfirm = {
                        showRemoveDialog = false
                        onRemoveConfirmed()
                    },
                    onDismiss = { showRemoveDialog = false }
                )
            }

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
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Card(
                            border = BorderStroke(2.dp, PortugalRedWine),
                            colors = CardDefaults.cardColors(containerColor = PortugalGreenFlag),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.size(110.dp)
                        ) {
                            if (isPreview) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.White.copy(alpha = 0.12f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "IMG",
                                        color = Color.White.copy(alpha = 0.85f),
                                        fontFamily = FontFamily.Default,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            } else {
                                AsyncImage(
                                    model = character.image,
                                    contentDescription = character.name,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
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

                        if (isPreview) {
                            Text(
                                text = if (character.isFavorite) "♥" else "♡",
                                fontSize = 32.sp,
                                color = if (character.isFavorite) Color.Red else Color.White,
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .clickable {
                                        if (character.isFavorite) showRemoveDialog =
                                            true else onAddFavorite()
                                    }
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.ic_favorite),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(if (character.isFavorite) Color.Red else Color.White),
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .clickable {
                                        if (character.isFavorite) showRemoveDialog =
                                            true else onAddFavorite()
                                    }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    InfoRow(label = "Species", value = character.species)
                    InfoRow(label = "Gender", value = character.gender)
                    InfoRow(label = "Origin", value = character.originName)
                    InfoRow(label = "Location", value = character.locationName)

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = "Episodes",
                        fontSize = 20.sp,
                        fontFamily = episodesTitleFont,
                        color = PortugalRedWine,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(3.dp))

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
                        val count = character.episodeUrls.size
                        val visibleCount = minOf(count, maxVisible)
                        val shouldScroll = count > maxVisible
                        val itemHeight = 48.dp
                        val itemSpacing = 12.dp
                        val padTop = 10.dp
                        val padBottom = 10.dp
                        val padStart = 12.dp
                        val padEnd = 12.dp
                        val contentHeight =
                            (itemHeight * visibleCount) +
                                    (itemSpacing * (visibleCount - 1).coerceAtLeast(0))
                        val cardHeight = padTop + padBottom + contentHeight

                        Card(
                            border = BorderStroke(2.dp, PortugalRedWine),
                            colors = CardDefaults.cardColors(containerColor = PortugalGreenFlag),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(cardHeight)
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .verticalScroll(episodesBoxScroll, enabled = shouldScroll)
                                        .padding(
                                            start = padStart,
                                            end = if (shouldScroll) (padEnd + 14.dp) else padEnd,
                                            top = padTop,
                                            bottom = padBottom
                                        ),
                                    verticalArrangement = Arrangement.spacedBy(itemSpacing)
                                ) {
                                    character.episodeUrls.forEach { url ->
                                        val ep =
                                            url.substringAfterLast("/", missingDelimiterValue = url)

                                        Surface(
                                            shape = RoundedCornerShape(10.dp),
                                            color = Color.White.copy(alpha = 0.08f),
                                            border = BorderStroke(
                                                1.dp,
                                                Color.White.copy(alpha = 0.18f)
                                            ),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(itemHeight)
                                                .clickable(
                                                    interactionSource = remember { MutableInteractionSource() },
                                                    indication = if (isPreview) null else ripple(
                                                        bounded = true
                                                    )
                                                ) { onEpisodeClick(url) }
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .padding(horizontal = 12.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "Episode $ep",
                                                    fontSize = 16.sp,
                                                    color = Color.White
                                                )

                                                if (isPreview) {
                                                    Text(
                                                        text = "›",
                                                        fontSize = 22.sp,
                                                        color = Color.White.copy(alpha = 0.85f)
                                                    )
                                                } else {
                                                    Image(
                                                        painter = painterResource(id = R.drawable.ic_chevron_right),
                                                        contentDescription = null,
                                                        colorFilter = ColorFilter.tint(
                                                            Color.White.copy(
                                                                alpha = 0.85f
                                                            )
                                                        )
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                if (shouldScroll && episodesBoxScroll.maxValue > 0) {
                                    val progress =
                                        (episodesBoxScroll.value.toFloat() / episodesBoxScroll.maxValue.toFloat())
                                            .coerceIn(0f, 1f)

                                    val thumbHeight = 20.dp
                                    val trackTop = padTop
                                    val trackBottom = padBottom
                                    val trackHeight = cardHeight - trackTop - trackBottom
                                    val thumbOffset =
                                        trackTop + (progress * (trackHeight - thumbHeight)).coerceAtLeast(
                                            0.dp
                                        )

                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.CenterEnd)
                                            .padding(
                                                end = 10.dp,
                                                top = trackTop,
                                                bottom = trackBottom
                                            )
                                            .fillMaxHeight()
                                            .width(3.dp)
                                            .background(
                                                Color.White.copy(alpha = 0.25f),
                                                RoundedCornerShape(99.dp)
                                            )
                                    )

                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(end = 10.dp)
                                            .offset(y = thumbOffset)
                                            .width(3.dp)
                                            .height(thumbHeight)
                                            .background(
                                                Color.White.copy(alpha = 0.8f),
                                                RoundedCornerShape(99.dp)
                                            )
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
            episodePopup = EpisodePopupState.Hidden,
            onEpisodeClick = {},
            onEpisodeDismiss = {},
            onRemoveConfirmed = {},
            onAddFavorite = {}
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
            episodePopup = EpisodePopupState.Hidden,
            onEpisodeClick = {},
            onEpisodeDismiss = {},
            onRemoveConfirmed = {},
            onAddFavorite = {}
        )
    }
}

@Preview(name = "DetailsScreen - Error", showBackground = true)
@Composable
private fun DetailsScreenPreviewError() {
    MarsRoverTheme {
        DetailsScreenContent(
            state = DetailsScreenState.Error("Load error"),
            episodePopup = EpisodePopupState.Hidden,
            onEpisodeClick = {},
            onEpisodeDismiss = {},
            onRemoveConfirmed = {},
            onAddFavorite = {}
        )
    }
}
