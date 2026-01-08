package com.portugal1576.marsrover.presentation.screens.details_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.portugal1576.marsrover.domain.model.FavoriteList
import com.portugal1576.marsrover.domain.usecase.GetCharacterByIdUseCase
import com.portugal1576.marsrover.domain.usecase.GetEpisodeByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailsScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val getCharacterById: GetCharacterByIdUseCase,
    private val getEpisodeById: GetEpisodeByIdUseCase,
    private val favoriteList: FavoriteList
) : ViewModel() {

    private val _state = MutableStateFlow<DetailsScreenState>(DetailsScreenState.Loading)
    val state: StateFlow<DetailsScreenState> = _state.asStateFlow()

    private val _episodePopup = MutableStateFlow<EpisodePopupState>(EpisodePopupState.Hidden)
    val episodePopup: StateFlow<EpisodePopupState> = _episodePopup.asStateFlow()

    private val id: Int = when (val raw = savedStateHandle.get<Any>("id")) {
        is Int -> raw
        is Long -> raw.toInt()
        is String -> raw.toIntOrNull() ?: -1
        else -> -1
    }

    private var favoriteIds: Set<Int> = emptySet()

    init {
        viewModelScope.launch {
            favoriteList.observeFavoriteIds().collect { ids ->
                favoriteIds = ids
                val cur = _state.value
                if (cur is DetailsScreenState.Loaded) {
                    _state.value = DetailsScreenState.Loaded(
                        cur.character.copy(isFavorite = favoriteIds.contains(cur.character.id))
                    )
                }
            }
        }

        if (id <= 0) {
            _state.value = DetailsScreenState.Error("Invalid id")
        } else {
            viewModelScope.launch {
                _state.value = DetailsScreenState.Loading
                runCatching { getCharacterById(id) }
                    .onSuccess { ch ->
                        _state.value = DetailsScreenState.Loaded(
                            ch.copy(isFavorite = favoriteIds.contains(ch.id))
                        )
                    }
                    .onFailure {
                        _state.value = DetailsScreenState.Error("Load error")
                    }
            }
        }
    }

    fun openEpisode(url: String) {
        val episodeId = url.substringAfterLast("/", "").toIntOrNull()
        if (episodeId == null || episodeId <= 0) {
            _episodePopup.value = EpisodePopupState.Error("Invalid episode id")
            return
        }

        viewModelScope.launch {
            runCatching { getEpisodeById(episodeId) }
                .onSuccess { episode ->
                    _episodePopup.value = EpisodePopupState.Loaded(episode)
                }
                .onFailure {
                    _episodePopup.value = EpisodePopupState.Error("Load error")
                }
        }
    }

    fun dismissEpisodePopup() {
        _episodePopup.value = EpisodePopupState.Hidden
    }

    fun toggleFavorite() {
        val cur = _state.value
        if (cur !is DetailsScreenState.Loaded) return
        viewModelScope.launch { favoriteList.toggle(cur.character) }
    }
}
