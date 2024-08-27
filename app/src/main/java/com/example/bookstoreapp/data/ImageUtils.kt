package com.example.bookstoreapp.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.example.bookstoreapp.R
import com.google.firebase.firestore.FirebaseFirestore
import java.io.ByteArrayOutputStream

object ImageUtils {
    fun bitmapToByteArray(context: Context, uri: Uri): ByteArray {
        val inputStream = context.contentResolver.openInputStream(uri)

        val bitmap = BitmapFactory.decodeStream(inputStream)
        val byteArrayOutputStream = ByteArrayOutputStream()

        bitmap.compress(
            Bitmap.CompressFormat.JPEG,
            10,
            byteArrayOutputStream
        )

        return byteArrayOutputStream.toByteArray()
    }
}