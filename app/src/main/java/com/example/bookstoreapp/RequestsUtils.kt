package com.example.bookstoreapp

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
    ): String {
        var r = PREFIX + "tasks?size=20"
        if (name != "") r += "&name=$name"
        if (priority != 0.toShort()) r += "&priorities=$priority"
        if (categoryIds.isNotEmpty()) r += "&categories=${categoryIds.joinToString(separator = ",") }"
        r += "&from=$from"
        r += "&to=$to"
        // TODO: not implemented in api
        // r += <isCompleted>
        r += "&order=" + when(sortBy){
            "Start date" -> "starts_at"
            "Edit date" -> "last_changed"
            "Name" -> "name"
            else -> "priority"
        }

        return r
    }
}