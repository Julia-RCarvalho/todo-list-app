package com.example.todolist.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.todolist.data.model.ToDoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(entity: ToDoEntity)

    @Delete
    suspend fun delete(entity: ToDoEntity)

    @Query("SELECT * FROM toDos")
    fun getAll(): Flow<List<ToDoEntity>>

    @Query("SELECT * FROM Todos WHERE id = :id")
    suspend fun getBy(id: Long): ToDoEntity?
}