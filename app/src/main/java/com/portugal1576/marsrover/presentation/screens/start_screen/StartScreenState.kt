package com.portugal1576.marsrover.presentation.screens.start_screen

import com.portugal1576.marsrover.domain.model.Character

sealed class StartScreenState {
    data object Loading : StartScreenState()
    data class Loaded(
        val items: List<Character>,
        val canLoadMore: Boolean,
        val isLoadingMore: Boolean
    ) : StartScreenState()
    data class Error(val message: String) : StartScreenState()
}
