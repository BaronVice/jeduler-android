package com.example.bookstoreapp

import android.util.Log
import com.example.bookstoreapp.data.Subtask
import com.example.bookstoreapp.data.Task
import kotlin.random.Random

object RequestsUtils {
//    private const val KEY = "CG-Jbv7Ee31WGnMgcdGjBzKYmt2"
//    private val CLIENT = OkHttpClient()
    private const val PREFIX = "https://.../jeduler/"

    fun buildSearchRequest(
        name: String,
        priority: Short,
        categoryIds: List<Short>,
        from: String,
        to: String,
        isCompleted: String,
        sortBy: String
    ): MutableMap<String, String> {
        val m = mutableMapOf<String, String>()
        if (name != "") m["name"] = name
        if (priority != 0.toShort()) m["priorities"] = priority.toString()
        if (categoryIds.isNotEmpty()) m["categories"] = categoryIds.joinToString(separator = ",")
        m["from"] = from
        m["to"] = to
        m["taskdone"] = isCompleted
        m["order"] = when(sortBy){
            "Start date" -> "starts"
            "Edit date" -> "changed"
            "Name" -> "name"
            else -> "priority"
        }
        m["page"] = "0"

        for (p in m){
            Log.d("MapOption", p.key + " " + p.value)
        }

        return m
    }
}