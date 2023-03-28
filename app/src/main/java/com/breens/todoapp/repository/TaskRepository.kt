package com.breens.todoapp.repository

import com.breens.todoapp.database.Task

class TaskRepository {

    suspend fun addTask(title: String, body: String) {
    }


    suspend fun getAllTasks(): List<Task> {
        return emptyList()
    }

    suspend fun deleteTask(id: Int) {
    }
}