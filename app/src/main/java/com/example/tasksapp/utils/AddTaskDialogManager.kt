package com.example.tasksapp.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.example.tasksapp.data.models.TaskData
import com.example.tasksapp.utils.DateTimeUtils.combineDateTimeToTargetMillis
import com.example.testdatabinding.databinding.InputDialogBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*

class AddTaskDialogManager(
    private val context: Context,
    private val fragmentManager: FragmentManager,
    private val onTaskCreated: (TaskData) -> Unit
) {
    private var selectedDateMillis = 0L
    private var selectedHour = 0
    private var selectedMinute = 0
    private var selectedDateString = ""

    fun showDialog() {
        val binding = InputDialogBinding.inflate(android.view.LayoutInflater.from(context))

        val dialog = MaterialAlertDialogBuilder(context)
            .setTitle("New task")
            .setView(binding.root)
            .setPositiveButton("OK", null)
            .setNegativeButton("Cancel", null)
            .create()

        dialog.setOnShowListener {
            val okButton = dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE)
            okButton.setOnClickListener {
                val title = binding.etTitle.textValue()
                val desc = binding.etDescription.textValue()

                if (title.isEmpty() || desc.isEmpty() || selectedDateMillis == 0L) {
                    Toast.makeText(context, "Please fill all fields!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                try {
                    val targetMillis = combineDateTimeToTargetMillis(
                        selectedDateMillis, selectedHour, selectedMinute
                    )
                    val task = TaskData(
                        id = UUID.randomUUID().toString(),
                        title = title,
                        description = desc,
                        time = targetMillis,
                        date = selectedDateString
                    )
                    Log.e("taskTimeTest","task: ${task.time}")
                    onTaskCreated(task)
                    dialog.dismiss()
                } catch (e: Exception) {
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
        dialog.show()

        binding.ibCalender.setOnClickListener {
            DateTimePickerHelper.showDatePicker(fragmentManager) { millis, formatted ->
                selectedDateMillis = millis
                selectedDateString = formatted
                Toast.makeText(context, "Date: $formatted", Toast.LENGTH_SHORT).show()
            }
        }

        binding.ibTime.setOnClickListener {
            DateTimePickerHelper.showTimePicker(fragmentManager) { hour, minute, formatted ->
                selectedHour = hour
                selectedMinute = minute
                Toast.makeText(context, "Time: $formatted", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
