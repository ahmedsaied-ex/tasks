package com.example.tasksapp.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TaskData(
    val id: String, val title: String, val description: String, val time: Long,val date:String
): Parcelable