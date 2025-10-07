package com.example.tasksapp.utils

import com.google.android.material.textfield.TextInputEditText

fun TextInputEditText.textValue(): String = this.text?.toString()?.trim().orEmpty()

