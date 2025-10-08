package com.example.tasksapp.ui.activity

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.example.tasksapp.constants.Constants
import com.example.testdatabinding.R
import com.example.testdatabinding.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        createNotificationChannel()

        // 2️⃣ طلب إذن الإشعارات (من Android 13 فما فوق)
        requestNotificationPermission()
    }

    private fun createNotificationChannel() {
        // القنوات مطلوبة فقط من Android 8 (Oreo) أو أحدث
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Constants.TASK_CHANEL_ID, // نفس ID اللي تستخدمه في ReminderReceiver
                Constants.TASK_REMINDERS, // اسم القناة اللي بيظهر للمستخدم
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "This channel is used for task reminder notifications"
            }

            // تسجيل القناة في النظام
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }
    }

}