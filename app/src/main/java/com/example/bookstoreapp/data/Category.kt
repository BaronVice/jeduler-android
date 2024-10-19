package com.example.bookstoreapp.data

import kotlinx.serialization.Serializable

data class Category(
    var id: Short,
    var name: String,
    var color: String
)