package com.example.tasksapp.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.SyncStateContract
import android.util.Log
import com.example.tasksapp.constants.Constants
import com.example.tasksapp.data.models.TaskData
import com.example.tasksapp.utils.ReminderScheduler
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.e("BootReceiver", "Device restarted - rescheduling tasks")

            val prefs = context.getSharedPreferences(Constants.TASK_PREFS, Context.MODE_PRIVATE)
            val json = prefs.getString(Constants.TASKS, "[]")
            val type = object : TypeToken<List<TaskData>>() {}.type
            val taskList: List<TaskData> = Gson().fromJson(json, type)


            for (task in taskList) {
                if (task.time > System.currentTimeMillis()) {
                    ReminderScheduler.scheduleTaskReminder(context, task)
                }
            }
        }
    }
}
