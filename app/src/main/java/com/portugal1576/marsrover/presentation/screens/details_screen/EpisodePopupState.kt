package com.portugal1576.marsrover.presentation.screens.details_screen

import com.portugal1576.marsrover.domain.model.Episode

sealed class EpisodePopupState {
    data object Hidden : EpisodePopupState()
    data class Loaded(val episode: Episode) : EpisodePopupState()
    data class Error(val message: String) : EpisodePopupState()
}
