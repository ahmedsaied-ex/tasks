package com.example.tasksapp.data.local

import android.content.Context
import android.util.Log
import com.example.tasksapp.constants.Constants
import com.example.tasksapp.data.models.TaskData
import com.google.gson.Gson
import androidx.core.content.edit
import com.google.gson.reflect.TypeToken

class TaskLocalStorage(context: Context) {

    private val prefs = context.getSharedPreferences(Constants.TASK_PREFS, Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveTasks(tasks: List<TaskData>) {
        try{
            val json = gson.toJson(tasks)
            prefs.edit { putString(Constants.TASKS, json) }
        }catch (e:Exception){
            Log.e("TaskLocalStorage", "Failed to save tasks: ${e.localizedMessage}")
        }
    }

    fun getTasks(): List<TaskData> {
        return try {
            val json = prefs.getString(Constants.TASKS, null)
            if (json != null) {
                val type = object : TypeToken<List<TaskData>>() {}.type
                val tasks = gson.fromJson<List<TaskData>>(json, type)
                Log.e("TaskLocalStorage", "getTasks: $json")
                tasks
            } else emptyList()
        } catch (e: Exception) {
            Log.e("TaskLocalStorage", "Failed to get tasks: ${e.localizedMessage}")
            emptyList()
        }
    }

}