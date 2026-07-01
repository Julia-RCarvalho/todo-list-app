package com.example.todolist.domain.usecase

import com.example.todolist.data.repository.ToDoRepository

class DeleteToDoUseCase(
    private val repository: ToDoRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.delete(id)
    }
}
