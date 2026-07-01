package com.example.todolist.domain.usecase

import com.example.todolist.data.repository.ToDoRepository
import com.example.todolist.domain.ToDo

class GetToDoUseCase(
    private val repository: ToDoRepository
) {
    suspend operator fun invoke(id: Long): ToDo? {
        return repository.getBy(id)
    }
}
