package com.example.bookstoreapp.login

import android.content.Context
import android.util.Log
import com.example.bookstoreapp.AppUtils.showToast
import com.google.firebase.auth.FirebaseAuth

object AuthUtils {
    fun parseTaskDate(date: String, delimiter: String = "   "): String{
        val parsed = date.split("+")
        return parsed[1] + delimiter + parsed[0]
    }
}