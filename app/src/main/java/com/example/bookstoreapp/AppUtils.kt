package com.example.bookstoreapp

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White

object AppUtils {
    fun showToast(context: Context, message: String) {
        Toast.makeText(
            context,
            message,
            Toast.LENGTH_SHORT,
        ).show()
    }

    fun hexToColor(hex: String): Int {
        return android.graphics.Color.parseColor(hex)
    }

    fun getContrastColor(hex: String): Color {
        val intColor = hexToColor(hex)
        val color = Color(intColor)

        val y: Float = (299 * color.red + 587 * color.green + 114 * color.blue)
        Log.d("COLOR_VALUE", y.toString())
        if (y >= 650)
            return Black
        else
            return White
    }
}