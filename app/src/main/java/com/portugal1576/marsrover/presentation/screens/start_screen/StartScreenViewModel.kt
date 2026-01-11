package com.portugal1576.marsrover.presentation.screens.start_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.portugal1576.marsrover.domain.model.Character
import com.portugal1576.marsrover.domain.model.FavoriteList
import com.portugal1576.marsrover.domain.usecase.GetCharactersUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StartScreenViewModel(
    private val getCharacters: GetCharactersUseCase,
    private val favoriteList: FavoriteList
) : ViewModel() {

    private val _state = MutableStateFlow<StartScreenState>(StartScreenState.Loading)
    val state: StateFlow<StartScreenState> = _state.asStateFlow()

    private var currentPage = 1
    private var canLoadMore = true
    private var isLoadingMore = false

    private var favoriteIds: Set<Int> = emptySet()
    private var query: String = ""
    private var sort: SortConfig = SortConfig()

    private var searchJob: Job? = null
    private var hasLoadedOnce = false

    init {
        viewModelScope.launch {
            favoriteList.observeFavoriteIds().collect { ids ->
                favoriteIds = ids
                val cur = _state.value
                if (cur is StartScreenState.Loaded) {
                    _state.value = cur.copy(
                        items = applySort(cur.items.map { it.copy(isFavorite = favoriteIds.contains(it.id)) })
                    )
                }
            }
        }
    }

    fun loadInitialIfNeeded() {
        if (hasLoadedOnce && _state.value is StartScreenState.Loaded) return
        hasLoadedOnce = true
        refreshCharacters()
    }

    fun onQueryChange(newQuery: String) {
        query = newQuery
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(350)
            refreshCharacters()
        }
    }

    fun setSortConfig(newSort: SortConfig) {
        sort = newSort
        val cur = _state.value
        if (cur is StartScreenState.Loaded) {
            _state.value = cur.copy(
                sort = sort,
                items = applySort(cur.items)
            )
        }
    }

    fun refreshCharacters() {
        currentPage = 1
        canLoadMore = true
        isLoadingMore = false

        viewModelScope.launch {
            _state.value = StartScreenState.Loading
            runCatching { getCharacters(page = currentPage, name = query.ifBlank { null }) }
                .onSuccess { page ->
                    canLoadMore = page.nextPage != null
                    val items = page.items
                        .map { it.copy(isFavorite = favoriteIds.contains(it.id)) }
                    _state.value = StartScreenState.Loaded(
                        items = applySort(items),
                        canLoadMore = canLoadMore,
                        isLoadingMore = false,
                        query = query,
                        sort = sort
                    )
                }
                .onFailure {
                    _state.value = StartScreenState.Error("Load error")
                }
        }
    }

    fun loadNextPage() {
        val cur = _state.value
        if (cur !is StartScreenState.Loaded) return
        if (!cur.canLoadMore) return
        if (isLoadingMore) return

        isLoadingMore = true
        _state.value = cur.copy(isLoadingMore = true)

        viewModelScope.launch {
            runCatching { getCharacters(page = currentPage + 1, name = query.ifBlank { null }) }
                .onSuccess { page ->
                    currentPage += 1
                    canLoadMore = page.nextPage != null
                    isLoadingMore = false

                    val merged = (cur.items + page.items)
                        .map { it.copy(isFavorite = favoriteIds.contains(it.id)) }

                    _state.value = cur.copy(
                        items = applySort(merged),
                        canLoadMore = canLoadMore,
                        isLoadingMore = false,
                        query = query,
                        sort = sort
                    )
                }
                .onFailure {
                    isLoadingMore = false
                    val now = _state.value
                    if (now is StartScreenState.Loaded) {
                        _state.value = now.copy(isLoadingMore = false)
                    }
                }
        }
    }

    fun toggleFavorite(character: Character) {
        viewModelScope.launch { favoriteList.toggle(character) }
    }

    private fun applySort(items: List<Character>): List<Character> {
        val keySelector: (Character) -> String = when (sort.key) {
            SortKey.NONE -> { c -> c.name.orEmpty() }
            SortKey.STATUS -> { c -> c.status.orEmpty() }
            SortKey.SPECIES -> { c -> c.species.orEmpty() }
            SortKey.GENDER -> { c -> c.gender.orEmpty() }
        }

        val normalized: (Character) -> String = { c -> keySelector(c).trim().lowercase() }

        val sorted = items.sortedWith(
            compareBy<Character> { normalized(it) }
                .thenBy { it.name.trim().lowercase() }
        )

        return if (sort.ascending) sorted else sorted.asReversed()
    }
}
