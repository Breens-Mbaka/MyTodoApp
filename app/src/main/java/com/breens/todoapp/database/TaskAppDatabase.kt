package com.breens.todoapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Task::class], version = 1)
abstract class TaskAppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        private var INSTANCE: TaskAppDatabase? = null

        fun getInstance(context: Context): TaskAppDatabase {
            if (INSTANCE == null) {
                synchronized(TaskAppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context = context.applicationContext,
                        klass = TaskAppDatabase::class.java,
                        name = "task.db"
                    ).build()
                }
            }
            return INSTANCE!!
        }
    }
}