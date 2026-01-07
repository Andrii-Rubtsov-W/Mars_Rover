package com.portugal1576.marsrover.presentation.screens.favorites_screen

import com.portugal1576.marsrover.domain.model.Character

sealed class FavoritesScreenState {
    data object Loading : FavoritesScreenState()
    data class Loaded(val character: List<Character>) : FavoritesScreenState()
    data class Error(val message: String) : FavoritesScreenState()
}