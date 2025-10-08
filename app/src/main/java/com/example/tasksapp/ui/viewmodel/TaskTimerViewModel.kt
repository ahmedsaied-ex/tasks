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
        startTimer()
    }

    private fun startTimer() {
        job?.cancel()
        job = viewModelScope.launch {
            try {
                // Check if target time is invalid or already passed
                val currentTime = System.currentTimeMillis()

                var diff = targetTime - currentTime

                Log.e("diff",diff.toString())

                if (diff<0) {
                    Log.e("diff","less than zero")
                    _remainingTime.value = "00:00:00"
                    _isFinished.value = true
                    return@launch
                }

                while (diff > 0) {
                    Log.e("diff","greater than zero")
                    _remainingTime.value = formatMillis(diff)
                    delay(1000)

                    diff = targetTime - System.currentTimeMillis()

                    // When time's up, set to zero and finish
                    if (diff <= 0) {
                        Log.e("diff","less than zero 22")
                        _remainingTime.value = "00:00:00"
                        _isFinished.value = true
                        break
                    }
                }
            } catch (e: Exception) {
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