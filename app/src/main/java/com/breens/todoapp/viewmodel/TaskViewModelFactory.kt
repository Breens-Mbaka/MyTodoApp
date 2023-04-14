package com.breens.todoapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.breens.todoapp.repository.TaskRepository

class TaskViewModelFactory(
    private val taskRepository: TaskRepository,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            return TaskViewModel(taskRepository, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}