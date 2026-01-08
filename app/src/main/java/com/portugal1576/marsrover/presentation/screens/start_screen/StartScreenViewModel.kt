package com.portugal1576.marsrover.presentation.screens.start_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.portugal1576.marsrover.domain.model.Character
import com.portugal1576.marsrover.domain.model.FavoriteList
import com.portugal1576.marsrover.domain.usecase.GetCharactersUseCase
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
    private var initialized = false

    init {
        viewModelScope.launch {
            favoriteList.observeFavoriteIds().collect { ids ->
                favoriteIds = ids
                val cur = _state.value
                if (cur is StartScreenState.Loaded) {
                    _state.value = cur.copy(
                        items = cur.items.map { it.copy(isFavorite = favoriteIds.contains(it.id)) }
                    )
                }
            }
        }
    }

    fun loadInitialIfNeeded() {
        if (initialized) return
        initialized = true

        val showLoading = _state.value !is StartScreenState.Loaded
        refreshCharacters(showLoading = showLoading)
    }

    fun refreshCharacters(showLoading: Boolean = true) {
        currentPage = 1
        canLoadMore = true
        isLoadingMore = false

        viewModelScope.launch {
            val prev = _state.value
            if (showLoading) {
                _state.value = StartScreenState.Loading
            } else if (prev is StartScreenState.Loaded) {
                _state.value = prev.copy(isLoadingMore = false)
            }

            runCatching { getCharacters(page = currentPage) }
                .onSuccess { page ->
                    canLoadMore = page.nextPage != null
                    _state.value = StartScreenState.Loaded(
                        items = page.items.map { it.copy(isFavorite = favoriteIds.contains(it.id)) },
                        canLoadMore = canLoadMore,
                        isLoadingMore = false
                    )
                }
                .onFailure {
                    val cur = _state.value
                    if (cur !is StartScreenState.Loaded) {
                        _state.value = StartScreenState.Error("Load error")
                    }
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
            runCatching { getCharacters(page = currentPage + 1) }
                .onSuccess { page ->
                    currentPage += 1
                    canLoadMore = page.nextPage != null
                    isLoadingMore = false

                    _state.value = cur.copy(
                        items = (cur.items + page.items).map { it.copy(isFavorite = favoriteIds.contains(it.id)) },
                        canLoadMore = canLoadMore,
                        isLoadingMore = false
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
        viewModelScope.launch {
            favoriteList.toggle(character)
        }
    }
}
