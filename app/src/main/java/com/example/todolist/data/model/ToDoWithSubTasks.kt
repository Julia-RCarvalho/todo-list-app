package com.example.todolist.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class ToDoWithSubTasks(
    @Embedded val todo: ToDoEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "todoId"
    )
    val subTasks: List<SubTaskEntity>
)
