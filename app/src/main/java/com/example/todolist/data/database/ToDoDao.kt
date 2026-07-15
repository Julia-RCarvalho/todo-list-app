package com.example.todolist.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.todolist.data.model.SubTaskEntity
import com.example.todolist.data.model.ToDoEntity
import com.example.todolist.data.model.ToDoWithSubTasks
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ToDoEntity): Long

    @Delete
    suspend fun delete(entity: ToDoEntity)

    @Transaction
    @Query("SELECT * FROM toDos")
    fun getAllWithSubTasks(): Flow<List<ToDoWithSubTasks>>

    @Query("SELECT * FROM toDos")
    fun getAllToDos(): Flow<List<ToDoEntity>>

    @Transaction
    @Query("SELECT * FROM Todos WHERE id = :id")
    suspend fun getByWithSubTasks(id: Long): ToDoWithSubTasks?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubTask(subTask: SubTaskEntity)

    @Delete
    suspend fun deleteSubTask(subTask: SubTaskEntity)

    @Query("UPDATE SubTasks SET isCompleted = :isCompleted WHERE id = :id")
    suspend fun updateSubTaskStatus(id: Long, isCompleted: Boolean)
}