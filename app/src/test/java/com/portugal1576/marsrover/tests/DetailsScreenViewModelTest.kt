package com.portugal1576.marsrover.tests

import androidx.lifecycle.SavedStateHandle
import com.portugal1576.marsrover.domain.model.Character
import com.portugal1576.marsrover.domain.model.Episode
import com.portugal1576.marsrover.domain.model.FavoriteList
import com.portugal1576.marsrover.domain.usecase.GetCharacterByIdUseCase
import com.portugal1576.marsrover.domain.usecase.GetEpisodeByIdUseCase
import com.portugal1576.marsrover.presentation.screens.details_screen.DetailsScreenState
import com.portugal1576.marsrover.presentation.screens.details_screen.DetailsScreenViewModel
import com.portugal1576.marsrover.presentation.screens.details_screen.EpisodePopupState
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailsScreenViewModelTest {

    @Test
    fun init_invalidId_setsErrorState() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)
        try {
            val getCharacterById = mockk<GetCharacterByIdUseCase>()
            val getEpisodeById = mockk<GetEpisodeByIdUseCase>()
            val favoriteList = mockk<FavoriteList>()
            val favFlow = MutableStateFlow<Set<Int>>(emptySet())

            every { favoriteList.observeFavoriteIds() } returns favFlow

            val vm = DetailsScreenViewModel(
                savedStateHandle = SavedStateHandle(mapOf("id" to -1)),
                getCharacterById = getCharacterById,
                getEpisodeById = getEpisodeById,
                favoriteList = favoriteList
            )

            runCurrent()

            val st = vm.state.value
            assertTrue(st is DetailsScreenState.Error)
            assertEquals("Invalid id", (st as DetailsScreenState.Error).message)

            coVerify(exactly = 0) { getCharacterById.invoke(any()) }
        } finally {
            Dispatchers.resetMain()
        }
    }

    @Test
    fun init_validId_loadsCharacter_success_setsLoadedState() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)
        try {
            val getCharacterById = mockk<GetCharacterByIdUseCase>()
            val getEpisodeById = mockk<GetEpisodeByIdUseCase>()
            val favoriteList = mockk<FavoriteList>()
            val favFlow = MutableStateFlow<Set<Int>>(emptySet())

            every { favoriteList.observeFavoriteIds() } returns favFlow

            val character = testCharacter(id = 1, isFavorite = false)
            coEvery { getCharacterById.invoke(1) } returns character

            val vm = DetailsScreenViewModel(
                savedStateHandle = SavedStateHandle(mapOf("id" to 1)),
                getCharacterById = getCharacterById,
                getEpisodeById = getEpisodeById,
                favoriteList = favoriteList
            )

            advanceUntilIdle()

            val st = vm.state.value as DetailsScreenState.Loaded
            assertEquals(1, st.character.id)
            assertEquals(false, st.character.isFavorite)

            coVerify(exactly = 1) { getCharacterById.invoke(1) }
        } finally {
            Dispatchers.resetMain()
        }
    }

    @Test
    fun favoriteIdsFlow_updatesLoadedCharacter_isFavorite() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)
        try {
            val getCharacterById = mockk<GetCharacterByIdUseCase>()
            val getEpisodeById = mockk<GetEpisodeByIdUseCase>()
            val favoriteList = mockk<FavoriteList>()
            val favFlow = MutableStateFlow<Set<Int>>(emptySet())

            every { favoriteList.observeFavoriteIds() } returns favFlow

            val character = testCharacter(id = 7, isFavorite = false)
            coEvery { getCharacterById.invoke(7) } returns character

            val vm = DetailsScreenViewModel(
                savedStateHandle = SavedStateHandle(mapOf("id" to 7)),
                getCharacterById = getCharacterById,
                getEpisodeById = getEpisodeById,
                favoriteList = favoriteList
            )

            advanceUntilIdle()

            var st = vm.state.value as DetailsScreenState.Loaded
            assertEquals(false, st.character.isFavorite)

            favFlow.value = setOf(7)
            runCurrent()

            st = vm.state.value as DetailsScreenState.Loaded
            assertEquals(true, st.character.isFavorite)

            favFlow.value = emptySet()
            runCurrent()

            st = vm.state.value as DetailsScreenState.Loaded
            assertEquals(false, st.character.isFavorite)
        } finally {
            Dispatchers.resetMain()
        }
    }

    @Test
    fun openEpisode_invalidUrl_setsEpisodePopupError_andDoesNotCallUseCase() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)
        try {
            val getCharacterById = mockk<GetCharacterByIdUseCase>()
            val getEpisodeById = mockk<GetEpisodeByIdUseCase>()
            val favoriteList = mockk<FavoriteList>()
            val favFlow = MutableStateFlow<Set<Int>>(emptySet())

            every { favoriteList.observeFavoriteIds() } returns favFlow
            coEvery { getCharacterById.invoke(1) } returns testCharacter(id = 1, isFavorite = false)

            val vm = DetailsScreenViewModel(
                savedStateHandle = SavedStateHandle(mapOf("id" to 1)),
                getCharacterById = getCharacterById,
                getEpisodeById = getEpisodeById,
                favoriteList = favoriteList
            )

            advanceUntilIdle()

            vm.openEpisode("https://rickandmortyapi.com/api/episode/")
            runCurrent()

            val popup = vm.episodePopup.value
            assertTrue(popup is EpisodePopupState.Error)
            assertEquals("Invalid episode id", (popup as EpisodePopupState.Error).message)

            coVerify(exactly = 0) { getEpisodeById.invoke(any()) }
        } finally {
            Dispatchers.resetMain()
        }
    }

    @Test
    fun openEpisode_success_setsEpisodePopupLoaded() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)
        try {
            val getCharacterById = mockk<GetCharacterByIdUseCase>()
            val getEpisodeById = mockk<GetEpisodeByIdUseCase>()
            val favoriteList = mockk<FavoriteList>()
            val favFlow = MutableStateFlow<Set<Int>>(emptySet())

            every { favoriteList.observeFavoriteIds() } returns favFlow
            coEvery { getCharacterById.invoke(1) } returns testCharacter(id = 1, isFavorite = false)

            val episode = testEpisode(id = 3)
            coEvery { getEpisodeById.invoke(3) } returns episode

            val vm = DetailsScreenViewModel(
                savedStateHandle = SavedStateHandle(mapOf("id" to 1)),
                getCharacterById = getCharacterById,
                getEpisodeById = getEpisodeById,
                favoriteList = favoriteList
            )

            advanceUntilIdle()

            vm.openEpisode("https://rickandmortyapi.com/api/episode/3")
            advanceUntilIdle()

            val popup = vm.episodePopup.value
            assertTrue(popup is EpisodePopupState.Loaded)
            assertEquals(3, (popup as EpisodePopupState.Loaded).episode.id)

            coVerify(exactly = 1) { getEpisodeById.invoke(3) }
        } finally {
            Dispatchers.resetMain()
        }
    }

    @Test
    fun openEpisode_failure_setsEpisodePopupError() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)
        try {
            val getCharacterById = mockk<GetCharacterByIdUseCase>()
            val getEpisodeById = mockk<GetEpisodeByIdUseCase>()
            val favoriteList = mockk<FavoriteList>()
            val favFlow = MutableStateFlow<Set<Int>>(emptySet())

            every { favoriteList.observeFavoriteIds() } returns favFlow
            coEvery { getCharacterById.invoke(1) } returns testCharacter(id = 1, isFavorite = false)

            coEvery { getEpisodeById.invoke(9) } throws RuntimeException("boom")

            val vm = DetailsScreenViewModel(
                savedStateHandle = SavedStateHandle(mapOf("id" to 1)),
                getCharacterById = getCharacterById,
                getEpisodeById = getEpisodeById,
                favoriteList = favoriteList
            )

            advanceUntilIdle()

            vm.openEpisode("https://rickandmortyapi.com/api/episode/9")
            advanceUntilIdle()

            val popup = vm.episodePopup.value
            assertTrue(popup is EpisodePopupState.Error)
            assertEquals("Load error", (popup as EpisodePopupState.Error).message)
        } finally {
            Dispatchers.resetMain()
        }
    }

    @Test
    fun dismissEpisodePopup_setsHidden() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)
        try {
            val getCharacterById = mockk<GetCharacterByIdUseCase>()
            val getEpisodeById = mockk<GetEpisodeByIdUseCase>()
            val favoriteList = mockk<FavoriteList>()
            val favFlow = MutableStateFlow<Set<Int>>(emptySet())

            every { favoriteList.observeFavoriteIds() } returns favFlow
            coEvery { getCharacterById.invoke(1) } returns testCharacter(id = 1, isFavorite = false)

            val episode = testEpisode(id = 1)
            coEvery { getEpisodeById.invoke(1) } returns episode

            val vm = DetailsScreenViewModel(
                savedStateHandle = SavedStateHandle(mapOf("id" to 1)),
                getCharacterById = getCharacterById,
                getEpisodeById = getEpisodeById,
                favoriteList = favoriteList
            )

            advanceUntilIdle()

            vm.openEpisode("https://rickandmortyapi.com/api/episode/1")
            advanceUntilIdle()

            assertTrue(vm.episodePopup.value is EpisodePopupState.Loaded)

            vm.dismissEpisodePopup()
            runCurrent()

            assertEquals(EpisodePopupState.Hidden, vm.episodePopup.value)
        } finally {
            Dispatchers.resetMain()
        }
    }

    @Test
    fun toggleFavorite_callsFavoriteListToggle_withLoadedCharacter() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)
        try {
            val getCharacterById = mockk<GetCharacterByIdUseCase>()
            val getEpisodeById = mockk<GetEpisodeByIdUseCase>()
            val favoriteList = mockk<FavoriteList>()
            val favFlow = MutableStateFlow<Set<Int>>(emptySet())

            every { favoriteList.observeFavoriteIds() } returns favFlow
            coEvery { favoriteList.toggle(any()) } just Runs

            val character = testCharacter(id = 1, isFavorite = false)
            coEvery { getCharacterById.invoke(1) } returns character

            val vm = DetailsScreenViewModel(
                savedStateHandle = SavedStateHandle(mapOf("id" to 1)),
                getCharacterById = getCharacterById,
                getEpisodeById = getEpisodeById,
                favoriteList = favoriteList
            )

            advanceUntilIdle()

            vm.toggleFavorite()
            advanceUntilIdle()

            coVerify(exactly = 1) { favoriteList.toggle(match { it.id == 1 }) }
        } finally {
            Dispatchers.resetMain()
        }
    }

    private fun testCharacter(id: Int, isFavorite: Boolean): Character = Character(
        id = id,
        name = "Rick Sanchez",
        image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
        status = "Alive",
        species = "Human",
        gender = "Male",
        originName = "Earth (C-137)",
        locationName = "Citadel of Ricks",
        episodeUrls = listOf(
            "https://rickandmortyapi.com/api/episode/1",
            "https://rickandmortyapi.com/api/episode/2"
        ),
        isFavorite = isFavorite
    )

    private fun testEpisode(id: Int): Episode = Episode(
        id = id,
        name = "Pilot",
        code = "S01E01",
        airDate = "December 2, 2013"
    )
}
