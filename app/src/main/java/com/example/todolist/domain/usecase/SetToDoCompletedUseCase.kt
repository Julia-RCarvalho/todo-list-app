package com.example.todolist.domain.usecase

import com.example.todolist.data.repository.ToDoRepository

class SetToDoCompletedUseCase(
    private val repository: ToDoRepository
) {
    suspend operator fun invoke(id: Long, isCompleted: Boolean) {
        repository.updateCompleted(id, isCompleted)
    }
}
