package com.example.bookstoreapp.data

import androidx.compose.runtime.Stable

data class Subtask(
    var name: String,
    var isCompleted: Boolean,
    var orderInList: Short
)
