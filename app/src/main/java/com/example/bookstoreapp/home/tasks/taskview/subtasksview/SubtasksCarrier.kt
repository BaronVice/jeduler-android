package com.example.bookstoreapp.home.tasks.taskview.subtasksview

import androidx.compose.runtime.Stable
import com.example.bookstoreapp.data.Subtask

@Stable
data class SubtasksCarrier(
    var subtasks: MutableList<Subtask>
)
