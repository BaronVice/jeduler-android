package com.example.bookstoreapp

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import com.example.bookstoreapp.data.Category
import java.util.Calendar
import kotlin.random.Random

object AppUtils {
    private val genericCategoryNames = listOf(
        "Fancy",
        "Awesome",
        "Magnificent",
        "Unbelievable",
        "Weird",
        "Uncanny",
        "Optimistic",
        "Pretty",
        "Mind blowing",
        "Fabulous",
        "Wonderful",
        "Empty",
        "Drop table",
        "Null",
        "Creative",
        "RuntimeException"
    )

    fun showToast(context: Context, message: String) {
        Toast.makeText(
            context,
            message,
            Toast.LENGTH_SHORT,
        ).show()
    }

    fun defaultTime(): Calendar {
        val currentTime = Calendar.getInstance()
        currentTime.isLenient = false

        currentTime.add(Calendar.MINUTE, 10)
        return currentTime
    }
    fun defaultDate(): Calendar {
        val currentTime = Calendar.getInstance()
        currentTime.isLenient = false

        currentTime.add(Calendar.HOUR, 24)
        return currentTime
    }


    fun getTimeFromTask(date: String) = date.split("+")[1]
    fun getDateFromTask(date: String) = date.split("+")[0]

    fun parseTaskDate(date: String, delimiter: String = "   "): String{
        val parsed = date.split("+")
        return parsed[1] + delimiter + parsed[0]
    }

    fun hexToColor(hex: String): Int {
        return android.graphics.Color.parseColor(hex)
    }

    fun getContrastColor(hex: String): Color {
        val intColor = hexToColor(hex)
        val color = Color(intColor)

        val y: Float = (299 * color.red + 587 * color.green + 114 * color.blue)
//        Log.d("COLOR_VALUE", y.toString())
        if (y >= 650)
            return Black
        else
            return White
    }

    fun getRandomCategoryName(
        existingCategories: List<Category>
    ): String {
        for (category in genericCategoryNames){
            if (!checkForCategoryNameConflict(category, existingCategories))
                return category
        }

        return genericCategoryNames.last()
    }

    private fun checkForCategoryNameConflict(
        name: String,
        existingCategories: List<Category>
    ): Boolean {
        if (existingCategories.find { c -> c.name == name } == null){
            return false
        }
        return true
    }

    fun checkForCategoryNameConflictOnEdit(
        name: String,
        existingCategories: List<Category>
    ): Boolean {
        if (existingCategories.count { c -> c.name == name } < 2){
            return false
        }
        return true
    }

    fun getRandomHex(): String {
        return "#${Random.nextInt(100000, 999999)}"
    }
}