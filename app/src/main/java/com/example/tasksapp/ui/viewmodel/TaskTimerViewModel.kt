package com.example.tasksapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
class TaskTimerViewModel(private val targetTime: Long) : ViewModel() {

    private val _remainingTime = MutableStateFlow("00:00:00")
    val remainingTime: StateFlow<String> = _remainingTime.asStateFlow()

    private val _isFinished = MutableStateFlow(false)
    val isFinished: StateFlow<Boolean> = _isFinished.asStateFlow()

    private var job: Job? = null

    init {
        // Calculate and set initial state immediately
        val currentTime = System.currentTimeMillis()
        val diff = targetTime - currentTime

        if (diff <= 0) {
            _remainingTime.value = "00:00:00"
            _isFinished.value = true
        } else {
            _remainingTime.value = formatMillis(diff)
            startTimer()
        }
    }

    private fun startTimer() {
        job?.cancel()
        job = viewModelScope.launch {
            try {
                while (true) {
                    delay(1000)

                    val currentTime = System.currentTimeMillis()
                    val diff = targetTime - currentTime

                    // When time's up, set to zero and finish
                    if (diff <= 0) {
                        Log.e("TaskTimer", "Timer finished")
                        _remainingTime.value = "00:00:00"
                        _isFinished.value = true
                        break
                    }

                    // Update remaining time
                    _remainingTime.value = formatMillis(diff)
                }
            } catch (e: Exception) {
                Log.e("TaskTimer", "Timer error: ${e.message}")
                _remainingTime.value = "00:00:00"
                _isFinished.value = true
            }
        }
    }

    private fun formatMillis(millis: Long): String {
        // Always clamp to zero to prevent negative values
        val clampedMillis = if (millis < 0) 0 else millis
        val totalSeconds = (clampedMillis / 1000).toInt()
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    override fun onCleared() {
        job?.cancel()
        super.onCleared()
    }

}