package com.example.bookstoreapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: Int,
    var name: String,
    var color: String
)