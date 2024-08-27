package com.example.bookstoreapp.data

import com.google.firebase.firestore.FirebaseFirestore

data class Book(
    val name: String = "",
    val description: String = "",
    val price: String = "",
    val category: String = "",
    val imageUrl: String = ""
){
    companion object{
        fun saveBook(fs: FirebaseFirestore, url: String){
            fs.collection("books")
                .document()
                .set(
                    Book(
                        "Clowns and balloons",
                        "Real tricks, no gimmicks",
                        "13.37",
                        "Comedy",
                        url
                    )
                )
        }
    }
}
