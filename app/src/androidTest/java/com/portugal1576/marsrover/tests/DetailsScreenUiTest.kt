package com.portugal1576.marsrover.tests

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.runtime.CompositionLocalProvider
import com.portugal1576.marsrover.domain.model.Character
import com.portugal1576.marsrover.domain.model.Episode
import com.portugal1576.marsrover.presentation.screens.details_screen.DetailsScreen
import com.portugal1576.marsrover.presentation.screens.details_screen.DetailsScreenState
import com.portugal1576.marsrover.presentation.screens.details_screen.EpisodePopupState
import com.portugal1576.marsrover.ui.theme.MarsRoverTheme
import org.junit.Rule
import org.junit.Test

class DetailsScreenUiTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun loading_showsProgress() {
        composeRule.setContent {
            MarsRoverTheme {
                CompositionLocalProvider(LocalInspectionMode provides true) {
                    DetailsScreen(
                        state = DetailsScreenState.Loading,
                        episodePopup = EpisodePopupState.Hidden,
                        onEpisodeClick = {},
                        onEpisodeDismiss = {},
                        onRemoveConfirmed = {},
                        onAddFavorite = {}
                    )
                }
            }
        }

        composeRule
            .onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate))
            .assertIsDisplayed()
    }

    @Test
    fun error_showsMessage() {
        composeRule.setContent {
            MarsRoverTheme {
                CompositionLocalProvider(LocalInspectionMode provides true) {
                    DetailsScreen(
                        state = DetailsScreenState.Error("Load error"),
                        episodePopup = EpisodePopupState.Hidden,
                        onEpisodeClick = {},
                        onEpisodeDismiss = {},
                        onRemoveConfirmed = {},
                        onAddFavorite = {}
                    )
                }
            }
        }

        composeRule.onNodeWithText("Load error").assertIsDisplayed()
    }

    @Test
    fun loaded_showsBasicInfoAndEpisodesTitle() {
        val ch = Character(
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
                "https://rickandmortyapi.com/api/episode/7",
            ),
            isFavorite = true
        )

        composeRule.setContent {
            MarsRoverTheme {
                CompositionLocalProvider(LocalInspectionMode provides true) {
                    DetailsScreen(
                        state = DetailsScreenState.Loaded(ch),
                        episodePopup = EpisodePopupState.Hidden,
                        onEpisodeClick = {},
                        onEpisodeDismiss = {},
                        onRemoveConfirmed = {},
                        onAddFavorite = {}
                    )
                }
            }
        }

        composeRule.onNodeWithText("Rick Sanchez").assertIsDisplayed()
        composeRule.onNodeWithText("Alive").assertIsDisplayed()

        composeRule.onNodeWithText("Human").assertIsDisplayed()
        composeRule.onNodeWithText("Male").assertIsDisplayed()
        composeRule.onNodeWithText("Earth (C-137)").assertIsDisplayed()
        composeRule.onNodeWithText("Citadel of Ricks").assertIsDisplayed()

        composeRule.onNodeWithText("Episodes").assertIsDisplayed()

        composeRule.onNodeWithText("Episode 1").assertIsDisplayed()
        composeRule.onNodeWithText("Episode 5").assertIsDisplayed()
    }

    @Test
    fun episodePopup_loaded_showsDialog() {
        val ch = Character(
            id = 1,
            name = "Rick Sanchez",
            image = "",
            status = "Alive",
            species = "Human",
            gender = "Male",
            originName = "Earth",
            locationName = "Citadel",
            episodeUrls = listOf("https://rickandmortyapi.com/api/episode/1"),
            isFavorite = false
        )

        val ep = Episode(
            id = 1,
            name = "Pilot",
            code = "S01E01",
            airDate = "December 2, 2013"
        )

        composeRule.setContent {
            MarsRoverTheme {
                CompositionLocalProvider(LocalInspectionMode provides true) {
                    DetailsScreen(
                        state = DetailsScreenState.Loaded(ch),
                        episodePopup = EpisodePopupState.Loaded(ep),
                        onEpisodeClick = {},
                        onEpisodeDismiss = {},
                        onRemoveConfirmed = {},
                        onAddFavorite = {}
                    )
                }
            }
        }

        composeRule.onNodeWithText("Pilot").assertIsDisplayed()
        composeRule.onNodeWithText("Code: S01E01\nAir date: December 2, 2013\nId: 1").assertIsDisplayed()
    }

    @Test
    fun episodePopup_error_showsDialog() {
        val ch = Character(
            id = 1,
            name = "Rick Sanchez",
            image = "",
            status = "Alive",
            species = "Human",
            gender = "Male",
            originName = "Earth",
            locationName = "Citadel",
            episodeUrls = emptyList(),
            isFavorite = false
        )

        composeRule.setContent {
            MarsRoverTheme {
                CompositionLocalProvider(LocalInspectionMode provides true) {
                    DetailsScreen(
                        state = DetailsScreenState.Loaded(ch),
                        episodePopup = EpisodePopupState.Error("Episode load error"),
                        onEpisodeClick = {},
                        onEpisodeDismiss = {},
                        onRemoveConfirmed = {},
                        onAddFavorite = {}
                    )
                }
            }
        }

        composeRule.onNodeWithText("Error").assertIsDisplayed()
        composeRule.onNodeWithText("Episode load error").assertIsDisplayed()
    }
}
