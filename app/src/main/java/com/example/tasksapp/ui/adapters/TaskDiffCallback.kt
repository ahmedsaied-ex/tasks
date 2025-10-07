package com.example.tasksapp.ui.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.tasksapp.data.models.TaskData

class TaskDiffCallback :DiffUtil.ItemCallback<TaskData>() {
    override fun areItemsTheSame(
        oldItem: TaskData,
        newItem: TaskData
    ): Boolean {
       return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: TaskData,
        newItem: TaskData
    ): Boolean {
        return oldItem == newItem
    }
}