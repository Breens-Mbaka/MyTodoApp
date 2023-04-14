package com.breens.todoapp.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.breens.todoapp.database.TaskAppDatabase
import com.breens.todoapp.repository.TaskRepository

class DeleteTasksWorker(
    appContext: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(appContext, workerParameters) {

    private val context = applicationContext

    private val taskAppDatabase = TaskAppDatabase
    private val taskRepository =
        TaskRepository(taskAppDatabase = taskAppDatabase.getInstance(context))

    // Delete all tasks
    override suspend fun doWork(): Result {
        return if (taskRepository.deleteAllTasks()) {
            Result.success()
        } else {
            Result.failure()
        }
    }
}