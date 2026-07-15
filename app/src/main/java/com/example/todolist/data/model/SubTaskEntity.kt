package com.example.todolist.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "SubTasks",
    foreignKeys = [
        ForeignKey(
            entity = ToDoEntity::class,
            parentColumns = ["id"],
            childColumns = ["todoId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("todoId")]
)
data class SubTaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val todoId: Long,
    val title: String,
    val isCompleted: Boolean
)
