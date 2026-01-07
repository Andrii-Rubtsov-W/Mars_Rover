package com.portugal1576.marsrover.presentation.screens.details_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.portugal1576.marsrover.domain.usecase.GetCharacterByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailsScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val getCharacterById: GetCharacterByIdUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<DetailsScreenState>(DetailsScreenState.Loading)
    val state: StateFlow<DetailsScreenState> = _state.asStateFlow()

    private val id: Int = savedStateHandle.get<String>("id")?.toIntOrNull() ?: -1

    init {
        if (id <= 0) {
            _state.value = DetailsScreenState.Error("Invalid id")
        } else {
            viewModelScope.launch {
                runCatching { getCharacterById(id) }
                    .onSuccess { _state.value = DetailsScreenState.Loaded(it) }
                    .onFailure { _state.value = DetailsScreenState.Error("Load error") }
            }
        }
    }
}