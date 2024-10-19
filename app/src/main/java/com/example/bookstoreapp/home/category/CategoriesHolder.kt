package com.example.bookstoreapp.home.category

import androidx.compose.runtime.Stable
import com.example.bookstoreapp.data.Category

@Stable
data class CategoriesHolder(
    val categories: MutableList<Category>
)
