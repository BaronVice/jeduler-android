package com.example.bookstoreapp.home.tasks.taskview

import androidx.compose.runtime.Stable
import com.example.bookstoreapp.data.Category

@Stable
data class CategoryList(
    val categories: List<Category>
)
