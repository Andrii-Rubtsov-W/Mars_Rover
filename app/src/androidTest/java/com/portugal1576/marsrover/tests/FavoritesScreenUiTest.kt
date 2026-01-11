package com.portugal1576.marsrover.tests

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.portugal1576.marsrover.domain.model.Character
import com.portugal1576.marsrover.presentation.navigation.Screens
import com.portugal1576.marsrover.presentation.screens.favorites_screen.FavoritesScreenContent
import com.portugal1576.marsrover.presentation.screens.favorites_screen.FavoritesScreenState
import com.portugal1576.marsrover.ui.theme.MarsRoverTheme
import org.junit.Rule
import org.junit.Test

class FavoritesScreenUiTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun loading_showsProgress() {
        composeRule.setContent {
            MarsRoverTheme {
                FavoritesScreenContent_ForTest(
                    state = FavoritesScreenState.Loading,
                    navigate = {}
                )
            }
        }

        composeRule.onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)).assertIsDisplayed()
    }

    @Test
    fun error_showsMessage() {
        composeRule.setContent {
            MarsRoverTheme {
                FavoritesScreenContent_ForTest(
                    state = FavoritesScreenState.Error("Favorite list is empty"),
                    navigate = {}
                )
            }
        }

        composeRule.onNodeWithText("Favorite list is empty").assertIsDisplayed()
    }

    @Test
    fun loaded_showsItems() {
        val items = listOf(
            Character(
                id = 1,
                name = "Rick Sanchez",
                image = "",
                status = "Alive",
                species = "Human",
                gender = "Male",
                originName = "Earth",
                locationName = "Citadel",
                episodeUrls = emptyList(),
                isFavorite = true
            ),
            Character(
                id = 2,
                name = "Morty Smith",
                image = "",
                status = "Alive",
                species = "Human",
                gender = "Male",
                originName = "Earth",
                locationName = "Citadel",
                episodeUrls = emptyList(),
                isFavorite = true
            )
        )

        composeRule.setContent {
            MarsRoverTheme {
                FavoritesScreenContent_ForTest(
                    state = FavoritesScreenState.Loaded(character = items),
                    navigate = {}
                )
            }
        }

        composeRule.onNodeWithText("Rick Sanchez").assertIsDisplayed()
        composeRule.onNodeWithText("Morty Smith").assertIsDisplayed()
    }

    @Test
    fun loaded_clickItem_triggersNavigateToDetails() {
        val items = listOf(
            Character(
                id = 7,
                name = "Test Character",
                image = "",
                status = "Alive",
                species = "Human",
                gender = "Male",
                originName = "Earth",
                locationName = "Earth",
                episodeUrls = emptyList(),
                isFavorite = true
            )
        )

        var navigated: Screens? = null

        composeRule.setContent {
            MarsRoverTheme {
                FavoritesScreenContent_ForTest(
                    state = FavoritesScreenState.Loaded(character = items),
                    navigate = { navigated = it }
                )
            }
        }

        composeRule.onNodeWithText("Test Character").performClick()
        assert(navigated is Screens.Details)
    }
}

@Composable
private fun FavoritesScreenContent_ForTest(
    state: FavoritesScreenState,
    navigate: (destinationScreen: Screens) -> Unit
) {
    FavoritesScreenContent(
        state = state,
        navigate = navigate
    )
}
