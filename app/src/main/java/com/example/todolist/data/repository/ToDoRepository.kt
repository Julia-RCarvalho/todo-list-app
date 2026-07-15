package com.example.todolist.data.repository

import com.example.todolist.domain.SubTask
import com.example.todolist.domain.ToDo
import kotlinx.coroutines.flow.Flow

interface ToDoRepository {

    suspend fun insert(title: String, description:String?, id: Long? = null)

    suspend fun updateCompleted (id: Long, isCompleted: Boolean)

    suspend fun delete(id: Long)

    fun getAll(): Flow<List<ToDo>>

    suspend fun getBy(id: Long): ToDo?

    suspend fun insertSubTask(todoId: Long, title: String)

    suspend fun updateSubTaskCompleted(id: Long, isCompleted: Boolean)

    suspend fun deleteSubTask(id: Long, todoId: Long, title: String, isCompleted: Boolean)
}