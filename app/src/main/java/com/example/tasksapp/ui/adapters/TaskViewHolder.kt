package com.example.tasksapp.ui.adapters

import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.example.tasksapp.data.models.TaskData

import com.example.tasksapp.ui.viewmodel.TaskTimerViewModel
import com.example.testdatabinding.databinding.TaskItemLayoutBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TaskViewHolder(
    private val binding: TaskItemLayoutBinding,
    private val onItemClicked: (TaskData) -> Unit,
    private val lifecycleScope: LifecycleCoroutineScope
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(task: TaskData) {
        binding.task = task
        binding.root.setOnClickListener { onItemClicked(task) }

        val timerViewModel = TaskTimerViewModel(task.time)

        lifecycleScope.launch {
            val remainingTime = task.time -  System.currentTimeMillis()
            if (remainingTime<=0){
                Log.e("remainingTime",remainingTime.toString())
                binding.tvTimer.text = "00:00:00"
            }
            else{
                timerViewModel.remainingTime.collectLatest { remaining ->
                    binding.tvTimer.text = remaining
                }
            }
        }

    }
}
