package com.example.bookstoreapp.data

import androidx.compose.runtime.Stable

@Stable
data class Task(
    var id: Int?,
    var name: String,
    var description: String,
    var taskDone: Boolean,
    var priority: Short,
    var categoryIds: List<Short>,
    var subtasks: List<Subtask>,
    var startsAt: String,
    var notifyAt: String // TODO: could be null
)
