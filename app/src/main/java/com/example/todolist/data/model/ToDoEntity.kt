package com.example.todolist.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ToDos")

data class ToDoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String?,
    val isCompleted: Boolean,
)