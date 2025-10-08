package com.example.tasksapp.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.tasksapp.data.models.TaskData
import com.example.tasksapp.data.repository.TaskRepository

import com.example.tasksapp.utils.ReminderScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaskDataViewModel(private val repo: TaskRepository) : ViewModel() {

    private val _tasks = MutableStateFlow<List<TaskData>>(emptyList())
    val tasks: StateFlow<List<TaskData>> = _tasks.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadTasks()
    }

    fun loadTasks() {
        viewModelScope.launch {
            try {
                _tasks.value = repo.getAllTasks()
                Log.e("taskssss2222",_tasks.value.toString())
            } catch (e: Exception) {
                _error.value = "Failed to load tasks"
            }
        }
    }

    fun addTask(task: TaskData,context: Context) {
        viewModelScope.launch {
            try {
                Log.e("taskssss",task.toString())
                repo.addTask(task)
                _tasks.value = _tasks.value + task
                ReminderScheduler.scheduleTaskReminder(
                    context = context,
                    task = task
                )
                Log.e("taskssss",_tasks.value.toString())
            } catch (e: Exception) {
                _error.value = "Failed to add task"
            }
        }
    }
    }




