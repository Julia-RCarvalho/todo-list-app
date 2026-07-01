package com.example.todolist.domain.usecase

data class ToDoUseCases(
    val getAllToDos: GetAllToDosUseCase,
    val getToDo: GetToDoUseCase,
    val saveToDo: SaveToDoUseCase,
    val deleteToDo: DeleteToDoUseCase,
    val setToDoCompleted: SetToDoCompletedUseCase
)
