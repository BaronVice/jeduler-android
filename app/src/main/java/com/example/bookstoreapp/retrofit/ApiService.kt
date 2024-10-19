package com.example.bookstoreapp.retrofit

import com.example.bookstoreapp.data.Category
import com.example.bookstoreapp.data.Task
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface ApiService {
    @Headers("ngrok-skip-browser-warning: 69420")
    @GET("tasks")
    suspend fun getTasks(
        @QueryMap options: Map<String, String>,
        @Header("Token") idToken: String
    ): List<Task>

    @Headers("ngrok-skip-browser-warning: 69420")
    @POST("tasks")
    suspend fun createTask( // todo: remove suspend add call
        @Body task: Task,
        @Header("Token") idToken: String
    ): Int

    @Headers("ngrok-skip-browser-warning: 69420")
    @PATCH("tasks")
    suspend fun updateTask( // todo: remove suspend add call
        @Body task: Task,
        @Header("Token") idToken: String
    )

    @Headers("ngrok-skip-browser-warning: 69420")
    @DELETE("tasks/{id}")
    suspend fun deleteTask(
        @Path("id") id: Int,
        @Header("Token") idToken: String
    )

    @Headers("ngrok-skip-browser-warning: 69420")
    @GET("categories")
    suspend fun getCategories(
        @Header("Token") idToken: String
    ): List<Category>

    @Headers("ngrok-skip-browser-warning: 69420")
    @POST("categories")
    fun createCategory(
        @Body category: Category,
        @Header("Token") idToken: String
    ): Call<Short>

    @Headers("ngrok-skip-browser-warning: 69420")
    @PATCH("categories")
    suspend fun updateCategory(
        @Body category: Category,
        @Header("Token") idToken: String
    )

    @Headers("ngrok-skip-browser-warning: 69420")
    @DELETE("categories/{id}")
    suspend fun deleteCategory(
        @Path("id") id: Short,
        @Header("Token") idToken: String
    )
}