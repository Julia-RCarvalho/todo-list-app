package com.example.todolist.data.repository

import com.example.todolist.data.database.ToDoDao
import com.example.todolist.data.model.SubTaskEntity
import com.example.todolist.data.model.ToDoEntity
import com.example.todolist.domain.SubTask
import com.example.todolist.domain.ToDo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ToDoRepositoryImpl(
    private val dao: ToDoDao
) : ToDoRepository {

    override suspend fun insert(title: String, description: String?, id: Long?) {
        withContext(Dispatchers.IO) {
            val entity = id?.let {
                dao.getByWithSubTasks(it)?.todo?.copy(
                    title = title,
                    description = description,
                )
            } ?: ToDoEntity(
                title = title,
                description = description,
                isCompleted = false,
            )
            dao.insert(entity)
        }
    }

    override suspend fun updateCompleted(id: Long, isCompleted: Boolean) {
        withContext(Dispatchers.IO) {
            val existingRelation = dao.getByWithSubTasks(id) ?: return@withContext
            val updateEntity = existingRelation.todo.copy(isCompleted = isCompleted)
            dao.insert(updateEntity)
        }
    }

    override suspend fun delete(id: Long) {
        withContext(Dispatchers.IO) {
            val existingRelation = dao.getByWithSubTasks(id) ?: return@withContext
            dao.delete(existingRelation.todo)
        }
    }

    override fun getAll(): Flow<List<ToDo>> {
        // Usando a busca leve para a lista principal
        return dao.getAllToDos().map { entities ->
            entities.map { entity ->
                ToDo(
                    id = entity.id,
                    title = entity.title,
                    description = entity.description,
                    isCompleted = entity.isCompleted,
                    subTasks = emptyList() // Lista principal não precisa de subtarefas agora
                )
            }
        }.flowOn(Dispatchers.Default) // Processa a lista fora da UI thread
    }

    override suspend fun getBy(id: Long): ToDo? {
        return withContext(Dispatchers.IO) {
            dao.getByWithSubTasks(id)?.let { relation ->
                ToDo(
                    id = relation.todo.id,
                    title = relation.todo.title,
                    description = relation.todo.description,
                    isCompleted = relation.todo.isCompleted,
                    subTasks = relation.subTasks.map { subEntity ->
                        SubTask(
                            id = subEntity.id,
                            todoId = subEntity.todoId,
                            title = subEntity.title,
                            isCompleted = subEntity.isCompleted
                        )
                    }
                )
            }
        }
    }

    override suspend fun insertSubTask(todoId: Long, title: String) {
        withContext(Dispatchers.IO) {
            dao.insertSubTask(
                SubTaskEntity(
                    todoId = todoId,
                    title = title,
                    isCompleted = false
                )
            )
        }
    }

    override suspend fun updateSubTaskCompleted(id: Long, isCompleted: Boolean) {
        withContext(Dispatchers.IO) {
            dao.updateSubTaskStatus(id, isCompleted)
        }
    }

    override suspend fun deleteSubTask(id: Long, todoId: Long, title: String, isCompleted: Boolean) {
        withContext(Dispatchers.IO) {
            dao.deleteSubTask(
                SubTaskEntity(
                    id = id,
                    todoId = todoId,
                    title = title,
                    isCompleted = isCompleted
                )
            )
        }
    }
}
