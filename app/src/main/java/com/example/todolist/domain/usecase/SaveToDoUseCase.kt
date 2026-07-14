package com.example.todolist.domain.usecase

import com.example.todolist.data.repository.ToDoRepository

class SaveToDoUseCase(
    private val repository: ToDoRepository
) {
    suspend operator fun invoke(title: String, description: String?, id: Long? = null) {
        if (title.isBlank()) {
            throw IllegalArgumentException("O título não pode estar vazio")
        }
        if (title.length > 100) {
            throw IllegalArgumentException("O título é muito longo (máx 100 caracteres)")
        }
        if ((description?.length ?: 0) > 1000) {
            throw IllegalArgumentException("A descrição é muito longa (máx 1000 caracteres)")
        }
        repository.insert(title, description, id)
    }
}
