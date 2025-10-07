package com.example.tasksapp.utils

import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.*

object DateTimePickerHelper {

    fun showDatePicker(
        fragmentManager: FragmentManager,
        onPicked: (millis: Long, formatted: String) -> Unit
    ) {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Date")
            .build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formatted = formatter.format(Date(selection))
            onPicked(selection, formatted)
        }

        datePicker.show(fragmentManager, "MATERIAL_DATE_PICKER")
    }

    fun showTimePicker(
        fragmentManager: FragmentManager,
        onPicked: (hour: Int, minute: Int, formatted: String) -> Unit
    ) {
        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setTitleText("Select Time")
            .setHour(12)
            .setMinute(0)
            .build()

        timePicker.addOnPositiveButtonClickListener {
            val hour = timePicker.hour
            val minute = timePicker.minute
            val formatted = String.format("%02d:%02d", hour, minute)
            onPicked(hour, minute, formatted)
        }

        timePicker.show(fragmentManager, "MATERIAL_TIME_PICKER")
    }
}
