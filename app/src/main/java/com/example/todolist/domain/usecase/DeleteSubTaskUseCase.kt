package com.example.todolist.domain.usecase

import com.example.todolist.data.repository.ToDoRepository
import com.example.todolist.domain.SubTask

class DeleteSubTaskUseCase(
    private val repository: ToDoRepository
) {
    suspend operator fun invoke(subTask: SubTask) {
        repository.deleteSubTask(subTask.id, subTask.todoId, subTask.title, subTask.isCompleted)
    }
}
