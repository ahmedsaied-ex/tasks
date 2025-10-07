package com.example.tasksapp.data.repository

import android.util.Log
import com.example.tasksapp.data.local.TaskLocalStorage
import com.example.tasksapp.data.models.TaskData

class TaskRepository(private val localStorage: TaskLocalStorage) {
    fun getAllTasks(): List<TaskData> {
        return try {
           val tasks= localStorage.getTasks()
            Log.e("TaskRepository", tasks.toString())
            tasks
        }catch (e:Exception){
            Log.e("TaskRepository", "Failed to get tasks: ${e.localizedMessage}")
            emptyList()
        }
    }

    fun addTask(task: TaskData) {
        try{
            val tasks = getAllTasks().toMutableList()
            tasks.add(task)
            localStorage.saveTasks(tasks)
            Log.e("TaskRepository", "Task added: $task")
        }catch (e:Exception){
            Log.e("TaskRepository", "Failed to add task: ${e.localizedMessage}")
        }
    }

}