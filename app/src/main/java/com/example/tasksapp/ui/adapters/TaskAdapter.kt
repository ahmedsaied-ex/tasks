package com.example.tasksapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.ListAdapter
import com.example.tasksapp.data.models.TaskData
import com.example.testdatabinding.databinding.TaskItemLayoutBinding

class TaskAdapter(private val lifecycleScope: LifecycleCoroutineScope, val onItemClicked: (TaskData) -> Unit) :
    ListAdapter<TaskData, TaskViewHolder>(TaskDiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): TaskViewHolder {
        val binding =
            TaskItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding, onItemClicked,lifecycleScope)
    }

    override fun onBindViewHolder(
        holder: TaskViewHolder, position: Int
    ) {
        holder.bind(getItem(position))
    }
}