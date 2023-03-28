package com.breens.todoapp.viewmodel

import com.breens.todoapp.database.Task

data class TaskUiState(
    val tasks: List<Task>? = null,
    val errorMessage: String? = null,
    val taskToBeDeleted: Task? = null,
)
