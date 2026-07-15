package com.example.todolist.domain.usecase

import com.example.todolist.data.repository.ToDoRepository

class ToggleSubTaskUseCase(
    private val repository: ToDoRepository
) {
    suspend operator fun invoke(id: Long, isCompleted: Boolean) {
        repository.updateSubTaskCompleted(id, isCompleted)
    }
}
