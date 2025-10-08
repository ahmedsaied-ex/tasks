package com.example.tasksapp.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import com.example.tasksapp.constants.Constants
import com.example.tasksapp.data.models.TaskData
import com.example.tasksapp.ui.notification.ReminderReceiver



object ReminderScheduler {

    fun scheduleTaskReminder(context: Context, task: TaskData) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val reminderTime = task.time - (5 * 60 * 1000 )// ⏰ 5 دقايق قبل الميعاد

        Log.e("timer_task",reminderTime.toString())
        if (reminderTime <= System.currentTimeMillis()) {
            Toast.makeText(context, "Task time has already passed", Toast.LENGTH_SHORT).show()
            return
        }


        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra(Constants.TASK_TITLE, task.title)
            putExtra(Constants.TASK_DESCRIPTION, task.description)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            task.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {

                    val alarmClockInfo = AlarmManager.AlarmClockInfo(reminderTime, pendingIntent)
                    alarmManager.setAlarmClock(alarmClockInfo, pendingIntent)
                } else {
                    val intentSettings = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                    intentSettings.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(intentSettings)
                }
            } else {
                alarmManager.setAlarmClock(
                    AlarmManager.AlarmClockInfo(reminderTime, pendingIntent),
                    pendingIntent
                )
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
            Toast.makeText(context, "Failed to schedule alarm: ${e.message}", Toast.LENGTH_LONG).show()
        }

    }

}
