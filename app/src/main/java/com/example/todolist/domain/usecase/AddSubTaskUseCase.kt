package com.example.todolist.domain.usecase

import com.example.todolist.data.repository.ToDoRepository

class AddSubTaskUseCase(
    private val repository: ToDoRepository
) {
    suspend operator fun invoke(todoId: Long, title: String) {
        if (title.isBlank()) return
        repository.insertSubTask(todoId, title)
    }
}
