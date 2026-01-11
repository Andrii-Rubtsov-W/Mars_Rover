package com.portugal1576.marsrover.tests

import com.portugal1576.marsrover.MainDispatcherRule
import com.portugal1576.marsrover.domain.model.Character
import com.portugal1576.marsrover.domain.model.FavoriteList
import com.portugal1576.marsrover.domain.model.Page
import com.portugal1576.marsrover.domain.usecase.GetCharactersUseCase
import com.portugal1576.marsrover.presentation.screens.start_screen.SortConfig
import com.portugal1576.marsrover.presentation.screens.start_screen.SortKey
import com.portugal1576.marsrover.presentation.screens.start_screen.StartScreenState
import com.portugal1576.marsrover.presentation.screens.start_screen.StartScreenViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StartScreenViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @get:Rule
    val mainRule = MainDispatcherRule(dispatcher)

    private fun ch(
        id: Int,
        name: String,
        status: String = "Alive",
        species: String = "Human",
        gender: String = "Male",
        isFavorite: Boolean = false
    ): Character {
        return Character(
            id = id,
            name = name,
            image = "https://example.com/$id.png",
            status = status,
            species = species,
            gender = gender,
            originName = "Earth",
            locationName = "Mars",
            episodeUrls = emptyList(),
            isFavorite = isFavorite
        )
    }

    @Test
    fun loadInitialIfNeeded_loads_once() = runTest(dispatcher) {
        val getCharacters = mockk<GetCharactersUseCase>()
        val favoriteList = mockk<FavoriteList>()

        val favFlow = MutableStateFlow<Set<Int>>(emptySet())
        every { favoriteList.observeFavoriteIds() } returns favFlow

        coEvery { getCharacters(page = 1, name = null) } returns Page(
            items = listOf(ch(2, "Morty"), ch(1, "Rick")),
            nextPage = 2
        )

        val vm = StartScreenViewModel(getCharacters = getCharacters, favoriteList = favoriteList)

        vm.loadInitialIfNeeded()
        advanceUntilIdle()

        val st1 = vm.state.value
        assertTrue(st1 is StartScreenState.Loaded)
        st1 as StartScreenState.Loaded

        assertTrue(st1.canLoadMore)
        assertFalse(st1.isLoadingMore)
        assertEquals("", st1.query)

        vm.loadInitialIfNeeded()
        advanceUntilIdle()

        coVerify(exactly = 1) { getCharacters(page = 1, name = null) }
    }

    @Test
    fun onQueryChange_debounces_350ms() = runTest(dispatcher) {
        val getCharacters = mockk<GetCharactersUseCase>()
        val favoriteList = mockk<FavoriteList>()

        val favFlow = MutableStateFlow<Set<Int>>(emptySet())
        every { favoriteList.observeFavoriteIds() } returns favFlow

        coEvery { getCharacters.invoke(1, null, null, null, null, null) } returns Page(
            items = listOf(ch(1, "Rick")),
            nextPage = null
        )
        coEvery { getCharacters.invoke(1, "r", null, null, null, null) } returns Page(
            items = listOf(ch(2, "Rick Prime")),
            nextPage = null
        )

        val vm = StartScreenViewModel(getCharacters = getCharacters, favoriteList = favoriteList)

        vm.loadInitialIfNeeded()
        runCurrent()

        vm.onQueryChange("r")
        runCurrent()

        coVerify(exactly = 1) { getCharacters.invoke(1, null, null, null, null, null) }
        coVerify(exactly = 0) { getCharacters.invoke(1, "r", null, null, null, null) }

        advanceTimeBy(349)
        runCurrent()

        coVerify(exactly = 0) { getCharacters.invoke(1, "r", null, null, null, null) }

        advanceTimeBy(1)
        runCurrent()

        coVerify(exactly = 1) { getCharacters.invoke(1, "r", null, null, null, null) }

        val st = vm.state.value as StartScreenState.Loaded
        assertEquals("r", st.query)
        assertEquals(listOf(2), st.items.map { it.id })
    }


    @Test
    fun favorite_ids_update_updates_items_isFavorite() = runTest(dispatcher) {
        val getCharacters = mockk<GetCharactersUseCase>()
        val favoriteList = mockk<FavoriteList>()

        val favFlow = MutableStateFlow<Set<Int>>(emptySet())
        every { favoriteList.observeFavoriteIds() } returns favFlow

        coEvery { getCharacters(page = 1, name = null) } returns Page(
            items = listOf(ch(1, "Rick"), ch(2, "Morty"), ch(3, "Summer")),
            nextPage = null
        )

        val vm = StartScreenViewModel(getCharacters = getCharacters, favoriteList = favoriteList)

        vm.loadInitialIfNeeded()
        advanceUntilIdle()

        favFlow.value = setOf(2, 3)
        advanceUntilIdle()

        val st = vm.state.value as StartScreenState.Loaded
        val map = st.items.associateBy { it.id }

        assertFalse(map.getValue(1).isFavorite)
        assertTrue(map.getValue(2).isFavorite)
        assertTrue(map.getValue(3).isFavorite)
    }

    @Test
    fun setSortConfig_sorts_by_status() = runTest(dispatcher) {
        val getCharacters = mockk<GetCharactersUseCase>()
        val favoriteList = mockk<FavoriteList>()

        val favFlow = MutableStateFlow<Set<Int>>(emptySet())
        every { favoriteList.observeFavoriteIds() } returns favFlow

        coEvery { getCharacters(page = 1, name = null) } returns Page(
            items = listOf(
                ch(1, "A", status = "Dead"),
                ch(2, "B", status = "Alive"),
                ch(3, "C", status = "unknown")
            ),
            nextPage = null
        )

        val vm = StartScreenViewModel(getCharacters = getCharacters, favoriteList = favoriteList)

        vm.loadInitialIfNeeded()
        advanceUntilIdle()

        vm.setSortConfig(SortConfig(key = SortKey.STATUS, ascending = true))
        advanceUntilIdle()

        val st = vm.state.value as StartScreenState.Loaded
        assertEquals(listOf(2, 1, 3), st.items.map { it.id })
    }

    @Test
    fun loadNextPage_merges_items() = runTest(dispatcher) {
        val getCharacters = mockk<GetCharactersUseCase>()
        val favoriteList = mockk<FavoriteList>()

        val favFlow = MutableStateFlow<Set<Int>>(emptySet())
        every { favoriteList.observeFavoriteIds() } returns favFlow

        coEvery { getCharacters(page = 1, name = null) } returns Page(
            items = listOf(ch(1, "Rick"), ch(2, "Morty")),
            nextPage = 2
        )
        coEvery { getCharacters(page = 2, name = null) } returns Page(
            items = listOf(ch(3, "Summer")),
            nextPage = null
        )

        val vm = StartScreenViewModel(getCharacters = getCharacters, favoriteList = favoriteList)

        vm.loadInitialIfNeeded()
        advanceUntilIdle()

        val st1 = vm.state.value as StartScreenState.Loaded
        assertTrue(st1.canLoadMore)
        assertEquals(2, st1.items.size)

        vm.loadNextPage()
        advanceUntilIdle()

        val st2 = vm.state.value as StartScreenState.Loaded
        assertFalse(st2.canLoadMore)
        assertFalse(st2.isLoadingMore)
        assertEquals(3, st2.items.size)

        coVerify(exactly = 1) { getCharacters(page = 1, name = null) }
        coVerify(exactly = 1) { getCharacters(page = 2, name = null) }
    }
}
