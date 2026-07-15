package com.example.todolist.ui.feature.addedit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.domain.SubTask
import com.example.todolist.domain.usecase.ToDoUseCases
import com.example.todolist.ui.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddEditViewModel(
    private val id: Long? = null,
    private val useCases: ToDoUseCases,
): ViewModel() {

    var title by mutableStateOf("")
    private set

    var description by mutableStateOf<String?>(null)
    private set

    var subTasks by mutableStateOf<List<SubTask>>(emptyList())
    private set

    var subTaskTitle by mutableStateOf("")
    private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        id?.let {
            viewModelScope.launch{
                val ToDo = useCases.getToDo(it)
                title = ToDo?.title?:""
                description = ToDo?.description
                subTasks = ToDo?.subTasks ?: emptyList()
            }
        }
    }

    fun onEvent(event: AddEditEvent) {
        when (event) {
            is AddEditEvent.TitleChanged -> {
                if (event.title.length <= 100) {
                    title = event.title
                }
            }

            is AddEditEvent.DescriptionChanged -> {
                if (event.description.length <= 1000) {
                    description = event.description
                }
            }

            AddEditEvent.Save -> {
                saveToDo()
            }

            AddEditEvent.Close -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.NavigateBack)
                }
            }

            is AddEditEvent.SubTaskTitleChanged -> {
                subTaskTitle = event.title
            }

            AddEditEvent.AddSubTask -> {
                addSubTask()
            }

            is AddEditEvent.DeleteSubTask -> {
                deleteSubTask(event.subTask)
            }

            is AddEditEvent.ToggleSubTask -> {
                toggleSubTask(event.subTask)
            }
        }
    }

    private fun addSubTask() {
        val todoId = id ?: return // Só permite adicionar subtarefa se a tarefa já existir
        if (subTaskTitle.isBlank()) return
        
        viewModelScope.launch {
            useCases.addSubTask(todoId, subTaskTitle)
            subTaskTitle = ""
            refreshToDo()
        }
    }

    private fun deleteSubTask(subTask: SubTask) {
        viewModelScope.launch {
            useCases.deleteSubTask(subTask)
            refreshToDo()
        }
    }

    private fun toggleSubTask(subTask: SubTask) {
        viewModelScope.launch {
            useCases.toggleSubTask(subTask.id, !subTask.isCompleted)
            refreshToDo()
        }
    }

    private suspend fun refreshToDo() {
        id?.let {
            val ToDo = useCases.getToDo(it)
            subTasks = ToDo?.subTasks ?: emptyList()
        }
    }

    private fun saveToDo() {
        viewModelScope.launch {
            try {
                useCases.saveToDo(title, description, id)
                _uiEvent.send(UiEvent.NavigateBack)
            } catch (e: IllegalArgumentException) {
                _uiEvent.send(UiEvent.ShowSnackbar(e.message ?: "Erro ao salvar"))
            }
        }
    }
}
