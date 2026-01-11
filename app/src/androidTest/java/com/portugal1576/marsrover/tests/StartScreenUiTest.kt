package com.portugal1576.marsrover.tests

import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import com.portugal1576.marsrover.domain.model.Character
import com.portugal1576.marsrover.presentation.screens.start_screen.SortConfig
import com.portugal1576.marsrover.presentation.screens.start_screen.SortKey
import com.portugal1576.marsrover.presentation.screens.start_screen.StartScreenContent
import com.portugal1576.marsrover.presentation.screens.start_screen.StartScreenState
import com.portugal1576.marsrover.ui.theme.MarsRoverTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger

class StartScreenUiTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun loading_showsHeaderAndProgress() {
        composeRule.setContent {
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

        composeRule.onNode(hasSetTextAction()).assertIsDisplayed()
        composeRule.onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)).assertIsDisplayed()
    }

    @Test
    fun error_showsHeaderAndMessage() {
        composeRule.setContent {
            MarsRoverTheme {
                StartScreenContent(
                    state = StartScreenState.Error("Something went wrong"),
                    navigate = {},
                    onLoadNextPage = {},
                    onToggleFavorite = {},
                    onQueryChange = {},
                    onApplySort = {}
                )
            }
        }

        composeRule.onNode(hasSetTextAction()).assertIsDisplayed()
        composeRule.onNodeWithText("Something went wrong").assertIsDisplayed()
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
                isFavorite = false
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
                isFavorite = false
            )
        )

        composeRule.setContent {
            MarsRoverTheme {
                StartScreenContent(
                    state = StartScreenState.Loaded(
                        items = items,
                        canLoadMore = false,
                        isLoadingMore = false,
                        query = "",
                        sort = SortConfig()
                    ),
                    navigate = {},
                    onLoadNextPage = {},
                    onToggleFavorite = {},
                    onQueryChange = {},
                    onApplySort = {}
                )
            }
        }

        composeRule.onNodeWithText("Rick Sanchez").assertIsDisplayed()
        composeRule.onNodeWithText("Morty Smith").assertIsDisplayed()
    }

    @Test
    fun typingInSearch_updatesQuery() {
        var uiState by mutableStateOf(
            StartScreenState.Loaded(
                items = emptyList(),
                canLoadMore = false,
                isLoadingMore = false,
                query = "",
                sort = SortConfig()
            ) as StartScreenState
        )

        composeRule.setContent {
            MarsRoverTheme {
                StartScreenContent(
                    state = uiState,
                    navigate = {},
                    onLoadNextPage = {},
                    onToggleFavorite = {},
                    onQueryChange = { q ->
                        val cur = uiState as StartScreenState.Loaded
                        uiState = cur.copy(query = q)
                    },
                    onApplySort = {}
                )
            }
        }

        composeRule.onNode(hasSetTextAction()).performTextClearance()
        composeRule.onNode(hasSetTextAction()).performTextInput("Rick")

        composeRule.onNodeWithText("Rick").assertIsDisplayed()
    }

    @Test
    fun filterClick_opensSortDialog_andApplyCallsCallback() {
        var applied: SortConfig? = null

        composeRule.setContent {
            MarsRoverTheme {
                StartScreenContent(
                    state = StartScreenState.Loaded(
                        items = emptyList(),
                        canLoadMore = false,
                        isLoadingMore = false,
                        query = "",
                        sort = SortConfig(key = SortKey.STATUS, ascending = true)
                    ),
                    navigate = {},
                    onLoadNextPage = {},
                    onToggleFavorite = {},
                    onQueryChange = {},
                    onApplySort = { applied = it }
                )
            }
        }

        composeRule.onNodeWithContentDescription("Filter").performClick()
        composeRule.onNodeWithText("Sorting").assertIsDisplayed()

        composeRule.onNodeWithText("Species").performClick()
        composeRule.onNodeWithText("Apply").performClick()

        assertEquals(SortKey.SPECIES, applied?.key)
    }

    @Test
    fun loaded_canLoadMore_triggersLoadNextPage() {
        val calls = AtomicInteger(0)

        composeRule.setContent {
            MarsRoverTheme {
                StartScreenContent(
                    state = StartScreenState.Loaded(
                        items = listOf(
                            Character(
                                id = 1,
                                name = "A",
                                image = "",
                                status = "Alive",
                                species = "Human",
                                gender = "Male",
                                originName = "Earth",
                                locationName = "Earth",
                                episodeUrls = emptyList(),
                                isFavorite = false
                            )
                        ),
                        canLoadMore = true,
                        isLoadingMore = false,
                        query = "",
                        sort = SortConfig()
                    ),
                    navigate = {},
                    onLoadNextPage = { calls.incrementAndGet() },
                    onToggleFavorite = {},
                    onQueryChange = {},
                    onApplySort = {}
                )
            }
        }

        composeRule.waitForIdle()
        assertEquals(1, calls.get())
    }
}
