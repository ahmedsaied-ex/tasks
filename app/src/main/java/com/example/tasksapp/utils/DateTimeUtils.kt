package com.example.tasksapp.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtils {

    fun combineDateTimeToTargetMillis(dateMillis: Long, hour: Int, minute: Int): Long {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = dateMillis
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val targetTime = calendar.timeInMillis
        val currentTime = System.currentTimeMillis()
        Log.e("taskTimeTest","targetTime: $targetTime currentTime: $currentTime")
        return if (targetTime > currentTime) targetTime else 0L


    }

}
