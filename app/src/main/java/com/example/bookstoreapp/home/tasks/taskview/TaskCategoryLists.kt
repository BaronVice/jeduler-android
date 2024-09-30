package com.example.bookstoreapp.home.tasks.taskview

import androidx.compose.runtime.Stable
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.bookstoreapp.data.Category
import com.example.bookstoreapp.data.Task

@Stable
data class TaskCategoryLists(
    val categories: List<Category>
)
