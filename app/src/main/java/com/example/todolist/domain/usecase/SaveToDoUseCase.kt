package com.example.todolist.domain.usecase

import com.example.todolist.data.repository.ToDoRepository

class SaveToDoUseCase(
    private val repository: ToDoRepository
) {
    suspend operator fun invoke(title: String, description: String?, id: Long? = null) {
        if (title.isBlank()) {
            throw IllegalArgumentException("O título não pode estar vazio")
        }
        repository.insert(title, description, id)
    }
}
