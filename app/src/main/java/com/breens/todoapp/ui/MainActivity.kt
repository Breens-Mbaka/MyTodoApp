package com.breens.todoapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.breens.todoapp.database.TaskAppDatabase
import com.breens.todoapp.repository.TaskRepository
import com.breens.todoapp.ui.components.AddTaskDialogComponent
import com.breens.todoapp.ui.components.EmptyComponent
import com.breens.todoapp.ui.components.TaskCardComponent
import com.breens.todoapp.ui.components.WelcomeMessageComponent
import com.breens.todoapp.ui.theme.MyTodoAppTheme
import com.breens.todoapp.viewmodel.TaskViewModel
import com.breens.todoapp.viewmodel.TaskViewModelFactory


class MainActivity : ComponentActivity() {

    private var taskViewModel: TaskViewModel? = null

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val taskAppDatabase = TaskAppDatabase

            val taskRepository = TaskRepository(taskAppDatabase = taskAppDatabase.getInstance(this))

            // Create an instance of the TaskViewModelFactory and pass it the repository instance
            val factory = TaskViewModelFactory(taskRepository)

            // Use the factory to get an instance of the TaskViewModel
            taskViewModel = factory.let {
                ViewModelProvider(
                    this,
                    it
                ).get(TaskViewModel::class.java)
            }

            val taskUiState = taskViewModel?.tasks?.collectAsState()?.value
            val dialogUiState = taskViewModel?.dialogUiState?.value

            MyTodoAppTheme {
                Scaffold(
                    floatingActionButton = {
                        ExtendedFloatingActionButton(
                            icon = {
                                Icon(
                                    Icons.Rounded.AddCircle,
                                    contentDescription = "Add Task",
                                    tint = Color.White
                                )
                            },
                            text = {
                                Text(
                                    text = "Add Task",
                                    color = Color.White
                                )
                            },
                            onClick = {
                                taskViewModel!!.showDialog(true)
                            },
                            modifier = Modifier.padding(horizontal = 12.dp),
                            backgroundColor = Color.Black,
                            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 8.dp)
                        )
                    },
                    floatingActionButtonPosition = FabPosition.Center,
                    backgroundColor = Color(0XFFFAFAFA)
                ) {
                    if (taskUiState?.tasks.isNullOrEmpty()) {
                        EmptyComponent()
                    }
                    if (dialogUiState?.showDialog == true) {
                        AddTaskDialogComponent(
                            setTaskTitle = { title ->
                                taskViewModel!!.setTaskTitle(title = title)
                            },
                            setTaskBody = { body ->
                                taskViewModel!!.setTaskBody(body = body)
                            },
                            saveTask = {
                                taskViewModel!!.addTask()
                            },
                            dialogUiState = dialogUiState,
                            closeDialog = {
                                taskViewModel!!.showDialog(false)
                            }
                        )
                    }
                    if (!taskUiState?.tasks.isNullOrEmpty()) {
                        LazyColumn(contentPadding = PaddingValues(14.dp)) {
                            item {
                                WelcomeMessageComponent()

                                Spacer(modifier = Modifier.height(30.dp))
                            }

                            items(taskUiState?.tasks ?: emptyList()) { task ->
                                TaskCardComponent(
                                    title = task.title,
                                    body = task.body,
                                    id = task.id,
                                    deleteTask = { id ->
                                        taskViewModel!!.deleteTask(id = id)
                                    }
                                )

                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}