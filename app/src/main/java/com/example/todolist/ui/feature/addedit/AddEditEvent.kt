package com.example.todolist.ui.feature.addedit

import com.example.todolist.domain.SubTask

sealed interface AddEditEvent {
    data class TitleChanged(val title: String) : AddEditEvent
    data class DescriptionChanged(val description: String) : AddEditEvent
    data object Save : AddEditEvent
    data object Close: AddEditEvent
    data class SubTaskTitleChanged(val title: String) : AddEditEvent
    data object AddSubTask : AddEditEvent
    data class ToggleSubTask(val subTask: SubTask) : AddEditEvent
    data class DeleteSubTask(val subTask: SubTask) : AddEditEvent
}
