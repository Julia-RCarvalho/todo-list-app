package com.example.todolist.domain.usecase

import com.example.todolist.data.repository.ToDoRepository
import com.example.todolist.domain.ToDo
import kotlinx.coroutines.flow.Flow

class GetAllToDosUseCase(
    private val repository: ToDoRepository
) {
    operator fun invoke(): Flow<List<ToDo>> {
        return repository.getAll()
    }
}
