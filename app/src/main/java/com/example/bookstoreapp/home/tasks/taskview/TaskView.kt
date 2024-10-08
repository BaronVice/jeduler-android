package com.example.bookstoreapp.home.tasks.taskview

import kotlinx.serialization.Serializable

@Serializable
data class TaskView(
    val id: Int,
    val url: String
)
