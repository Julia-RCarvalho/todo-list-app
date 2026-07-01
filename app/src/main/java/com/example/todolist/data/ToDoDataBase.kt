package com.example.todolist.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ToDoEntity::class],
    version = 1,
)

abstract class ToDoDataBase : RoomDatabase() {
    abstract val ToDoDao: ToDoDao
}

object ToDoDataBaseProvider {

    @Volatile
    private var INSTANCE: ToDoDataBase? = null

    fun provide(context: Context): ToDoDataBase{
        return INSTANCE ?: synchronized(this){
            val instance = Room.databaseBuilder(
                context.applicationContext,
                ToDoDataBase::class.java,
                "toDo-app"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}