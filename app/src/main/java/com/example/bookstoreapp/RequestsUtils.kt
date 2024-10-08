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

    fun getTasks(url: String): List<Task> {
        return listOf(
            Task(
                1,
                "Weird1",
                "Aboba",
                Random.nextBoolean(),
                Random.nextInt(1,4).toShort(),
                listOf(),
                listOf(
                    Subtask(
                        "Just",
                        true,
                        1
                    ),
                    Subtask(
                        "Leave",
                        true,
                        2
                    ),
                    Subtask(
                        "Me",
                        true,
                        3
                    ),
                    Subtask(
                        "Out",
                        true,
                        4
                    ),
                    Subtask(
                        "To",
                        true,
                        5
                    ),
                    Subtask(
                        "Dry",
                        true,
                        6
                    )
                ),
                "22.10.2024+09:07",
                "22.10.2024+09:07"
            ),
            Task(
                2,
                "Weird2",
                "Aboba",
                Random.nextBoolean(),
                Random.nextInt(1,4).toShort(),
                listOf(),
                listOf(),
                "22.10.2024+09:07",
                "22.10.2024+09:07"
            ),
            Task(
                3,
                "Weird3",
                "Aboba",
                Random.nextBoolean(),
                Random.nextInt(1,4).toShort(),
                listOf(),
                listOf(),
                "22.10.2024+09:07",
                "22.10.2024+09:07"
            ),
            Task(
                4,
                "Weird4",
                "Aboba",
                Random.nextBoolean(),
                Random.nextInt(1,4).toShort(),
                listOf(),
                listOf(),
                "22.10.2024+09:07",
                "22.10.2024+09:07"
            ),
            Task(
                5,
                "Weird5",
                "Aboba",
                Random.nextBoolean(),
                Random.nextInt(1,4).toShort(),
                listOf(),
                listOf(),
                "22.10.2024+09:07",
                "22.10.2024+09:07"
            ),
            Task(
                6,
                "Weird6",
                "Aboba",
                Random.nextBoolean(),
                Random.nextInt(1,4).toShort(),
                listOf(),
                listOf(),
                "22.10.2024+09:07",
                "22.10.2024+09:07"
            ),
            Task(
                7,
                "Weird7",
                "Aboba",
                Random.nextBoolean(),
                Random.nextInt(1,4).toShort(),
                listOf(),
                listOf(),
                "22.10.2024+09:07",
                "22.10.2024+09:07"
            ),
            Task(
                8,
                "Weird8",
                "Aboba",
                Random.nextBoolean(),
                Random.nextInt(1,4).toShort(),
                listOf(),
                listOf(),
                "22.10.2024+09:07",
                "22.10.2024+09:07"
            ),
            Task(
                9,
                "Weird9",
                "Aboba",
                Random.nextBoolean(),
                Random.nextInt(1,4).toShort(),
                listOf(),
                listOf(),
                "22.10.2024+09:07",
                "22.10.2024+09:07"
            ),
            Task(
                10,
                "Weird10",
                "Aboba",
                Random.nextBoolean(),
                Random.nextInt(1,4).toShort(),
                listOf(),
                listOf(),
                "22.10.2024+09:07",
                "22.10.2024+09:07"
            )
        )
    }
}