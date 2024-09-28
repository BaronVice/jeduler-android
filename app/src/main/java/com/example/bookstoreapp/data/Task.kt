package com.example.bookstoreapp.data

data class Task(
    val id: Int,
    var name: String,
    var description: String,
    var taskDone: Boolean,
    var priority: Short,
    var categoryIds: List<Short>,
    var subtasks: List<Subtask>,
    var startsAt: String,
    var notifyAt: String // TODO: could be null
)
