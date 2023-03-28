package com.breens.todoapp.viewmodel

data class DialogUiState(
    val showDialog: Boolean = false,
    val taskTitle: String = "",
    val taskBody: String = ""
)
