package com.example.todolist.ui

sealed interface UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent
    data object NavigateBack: UiEvent
    data class Navigate<T : Any>(val route: T) : UiEvent
}