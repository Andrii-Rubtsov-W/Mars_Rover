package com.portugal1576.marsrover.presentation.screens.favorites_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.portugal1576.marsrover.domain.model.FavoriteList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavoritesScreenViewModel(
    private val favoriteList: FavoriteList
) : ViewModel() {

    private val _state = MutableStateFlow<FavoritesScreenState>(FavoritesScreenState.Loading)
    val state: StateFlow<FavoritesScreenState> = _state

    fun refreshPlayers() {
        viewModelScope.launch {
            favoriteList.observeFavorites().collect { list ->
                _state.value = if (list.isEmpty()) {
                    FavoritesScreenState.Error("Favorite list is empty")
                } else {
                    FavoritesScreenState.Loaded(character = list)
                }
            }
        }
    }
}
