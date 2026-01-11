package com.portugal1576.marsrover.tests

import com.portugal1576.marsrover.domain.model.Character
import com.portugal1576.marsrover.domain.model.FavoriteList
import com.portugal1576.marsrover.presentation.screens.favorites_screen.FavoritesScreenState
import com.portugal1576.marsrover.presentation.screens.favorites_screen.FavoritesScreenViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class FavoritesScreenViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val favoriteList: FavoriteList = mockk()

    @Test
    fun initialState_isLoading() {
        val vm = FavoritesScreenViewModel(favoriteList)
        assertTrue(vm.state.value is FavoritesScreenState.Loading)
    }

    @Test
    fun refreshPlayers_whenEmpty_emitsError() = runTest {
        every { favoriteList.observeFavorites() } returns flowOf(emptyList())

        val vm = FavoritesScreenViewModel(favoriteList)
        vm.refreshPlayers()

        advanceUntilIdle()

        val s = vm.state.value
        assertTrue(s is FavoritesScreenState.Error)
        assertEquals("Favorite list is empty", (s as FavoritesScreenState.Error).message)
    }

    @Test
    fun refreshPlayers_whenNotEmpty_emitsLoaded() = runTest {
        val items = listOf(fakeCharacter(id = 1), fakeCharacter(id = 2))
        every { favoriteList.observeFavorites() } returns flowOf(items)

        val vm = FavoritesScreenViewModel(favoriteList)
        vm.refreshPlayers()

        advanceUntilIdle()

        val s = vm.state.value
        assertTrue(s is FavoritesScreenState.Loaded)
        assertEquals(items, (s as FavoritesScreenState.Loaded).character)
    }

    @Test
    fun refreshPlayers_updatesWhenFlowEmitsAgain() = runTest {
        val flow = MutableSharedFlow<List<Character>>(replay = 0)
        every { favoriteList.observeFavorites() } returns flow

        val vm = FavoritesScreenViewModel(favoriteList)
        vm.refreshPlayers()
        advanceUntilIdle()

        flow.emit(listOf(fakeCharacter(id = 10)))
        advanceUntilIdle()

        assertTrue(vm.state.value is FavoritesScreenState.Loaded)
        assertEquals(1, (vm.state.value as FavoritesScreenState.Loaded).character.size)

        flow.emit(emptyList())
        advanceUntilIdle()

        assertTrue(vm.state.value is FavoritesScreenState.Error)
        assertEquals(
            "Favorite list is empty",
            (vm.state.value as FavoritesScreenState.Error).message
        )
    }

    private fun fakeCharacter(id: Int): Character = Character(
        id = id,
        name = "Name $id",
        image = "https://example.com/$id.png",
        status = "Alive",
        species = "Human",
        gender = "Male",
        originName = "Earth",
        locationName = "Mars",
        episodeUrls = emptyList(),
        isFavorite = true
    )
}

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    private val dispatcher: TestDispatcher = StandardTestDispatcher()
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}
