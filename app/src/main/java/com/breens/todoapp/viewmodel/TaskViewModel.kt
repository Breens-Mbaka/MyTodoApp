package com.breens.todoapp.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breens.todoapp.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaskViewModel(private val taskRepository: TaskRepository) : ViewModel() {

    private val _tasks = MutableStateFlow(TaskUiState())
    val tasks: StateFlow<TaskUiState> = _tasks.asStateFlow()

    private val _dialogUiState = mutableStateOf(DialogUiState())
    val dialogUiState: State<DialogUiState> = _dialogUiState

    init {
        getAllTasks()
    }

    private fun getAllTasks() {
        viewModelScope.launch {
            val allTasks = taskRepository.getAllTasks()

            _tasks.value = tasks.value.copy(
                tasks = allTasks
            )
        }
    }

    fun addTask() {
        viewModelScope.launch {
            val title = dialogUiState.value.taskTitle
            val body = dialogUiState.value.taskBody

            taskRepository.addTask(title = title, body = body)

            getAllTasks()
        }
    }

    fun deleteTask(id: Int) {
        viewModelScope.launch {
            taskRepository.deleteTask(
                id = id
            )

            getAllTasks()
        }
    }

    fun setTaskTitle(title: String) {
        _dialogUiState.value = dialogUiState.value.copy(
            taskTitle = title
        )
    }

    fun setTaskBody(body: String) {
        _dialogUiState.value = dialogUiState.value.copy(
            taskBody = body
        )
    }

    fun showDialog(show: Boolean) {
        _dialogUiState.value = dialogUiState.value.copy(
            showDialog = show
        )
    }
}