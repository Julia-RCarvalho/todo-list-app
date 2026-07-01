package com.example.todolist.ui.feature.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.domain.usecase.ToDoUseCases
import com.example.todolist.navigation.AddEditRoute
import com.example.todolist.ui.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ListViewModel(
    private val useCases: ToDoUseCases,
) : ViewModel() {

    val todos = useCases.getAllToDos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: ListEvent) {
        when (event) {
            is ListEvent.Delete -> {
                delete(event.id)
            }

            is ListEvent.CompleteChanged -> {
                completeChanged(event.id, event.isCompleted)
            }

            is ListEvent.AddEdit -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.Navigate(AddEditRoute(event.id)))
                }
            }
        }
    }

    private fun delete(id: Long) {
        viewModelScope.launch {
            useCases.deleteToDo(id)
            _uiEvent.send(UiEvent.ShowSnackbar("Tarefa removida com sucesso"))
        }
    }

    private fun completeChanged(id: Long, isCompleted: Boolean) {
        viewModelScope.launch {
            useCases.setToDoCompleted(id, isCompleted)
        }
    }
}
