package com.portugal1576.marsrover.presentation.screens.details_screen

import com.portugal1576.marsrover.domain.model.Character

sealed class DetailsScreenState {
    data object Loading : DetailsScreenState()
    data class Loaded(val character: Character) : DetailsScreenState()
    data class Error(val message: String) : DetailsScreenState()
}