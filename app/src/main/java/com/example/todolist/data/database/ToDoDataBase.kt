package com.example.todolist.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todolist.data.database.ToDoDao
import com.example.todolist.data.model.SubTaskEntity
import com.example.todolist.data.model.ToDoEntity

@Database(
    entities = [ToDoEntity::class, SubTaskEntity::class],
    version = 2,
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
            ).fallbackToDestructiveMigration()
                .build()
            INSTANCE = instance
            instance
        }
    }
}