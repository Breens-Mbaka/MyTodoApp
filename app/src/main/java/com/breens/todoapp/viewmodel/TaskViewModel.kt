package com.breens.todoapp.viewmodel

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.breens.todoapp.repository.TaskRepository
import com.breens.todoapp.workers.DELETE_ALL_TASKS_WORKER
import com.breens.todoapp.workers.DeleteTasksWorker
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaskViewModel(private val taskRepository: TaskRepository, application: Application) :
    ViewModel() {

    private val _tasks = MutableStateFlow(TaskUiState())
    val tasks: StateFlow<TaskUiState> = _tasks.asStateFlow()

    private val _dialogUiState = mutableStateOf(DialogUiState())
    val dialogUiState: State<DialogUiState> = _dialogUiState

    private val workManager = WorkManager.getInstance(application)

    private val constraints = Constraints.Builder()
        .setRequiresBatteryNotLow(true)
        .build()

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

    internal fun deleteAllTasks() {
        val deleteAllTasksWorkerRequest =
            OneTimeWorkRequestBuilder<DeleteTasksWorker>()
                .setConstraints(constraints)
                .build()

        workManager.enqueueUniqueWork(
            DELETE_ALL_TASKS_WORKER,
            ExistingWorkPolicy.REPLACE,
            deleteAllTasksWorkerRequest
        )

//        workManager.enqueue(deleteAllTasksWorkerRequest)

    }

    internal fun deleteAllTaskAfterCertainDuration() {
        val deleteAllTaskAfterCertainDuration =
            PeriodicWorkRequestBuilder<DeleteTasksWorker>(
                repeatInterval = 15,
                repeatIntervalTimeUnit = TimeUnit.MINUTES
            )
                .setInitialDelay(
                    duration = 15,
                    timeUnit = TimeUnit.MINUTES
                )
                .setConstraints(constraints)
                .build()

        workManager.enqueue(deleteAllTaskAfterCertainDuration)
    }
}