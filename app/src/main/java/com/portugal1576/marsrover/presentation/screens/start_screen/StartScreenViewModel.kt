package com.portugal1576.marsrover.presentation.screens.start_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.portugal1576.marsrover.domain.model.Character
import com.portugal1576.marsrover.domain.model.FavoriteList
import com.portugal1576.marsrover.domain.usecase.GetCharactersUseCase
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class StartScreenViewModel(
    private val getCharacters: GetCharactersUseCase,
    private val favoriteList: FavoriteList
) : ViewModel() {

    private val _state = MutableStateFlow<StartScreenState>(StartScreenState.Loading)
    val state: StateFlow<StartScreenState> = _state.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private var currentPage = 1
    private var favoriteIds: Set<Int> = emptySet()
    private var hasLoadedOnce = false
    private var isLoadingMore = false

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

        viewModelScope.launch {
            _searchQuery
                .debounce(350)
                .distinctUntilChanged()
                .collectLatest {
                    if (hasLoadedOnce) refreshCharacters()
                }
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun loadInitialIfNeeded() {
        if (hasLoadedOnce) return
        hasLoadedOnce = true
        refreshCharacters()
    }

    fun refreshCharacters() {
        val name = _searchQuery.value.trim().ifBlank { null }
        currentPage = 1
        isLoadingMore = false

        viewModelScope.launch {
            _state.value = StartScreenState.Loading
            runCatching { getCharacters(page = 1, name = name) }
                .onSuccess { page ->
                    val canLoadMore = page.nextPage != null
                    _state.value = StartScreenState.Loaded(
                        items = page.items.map { it.copy(isFavorite = favoriteIds.contains(it.id)) },
                        canLoadMore = canLoadMore,
                        isLoadingMore = false
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

        val name = _searchQuery.value.trim().ifBlank { null }
        isLoadingMore = true
        _state.value = cur.copy(isLoadingMore = true)

        viewModelScope.launch {
            runCatching { getCharacters(page = currentPage + 1, name = name) }
                .onSuccess { page ->
                    currentPage += 1
                    val canLoadMore = page.nextPage != null
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
        viewModelScope.launch { favoriteList.toggle(character) }
    }
}
