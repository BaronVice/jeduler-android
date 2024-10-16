package com.example.bookstoreapp.retrofit

import android.util.Log
import com.example.bookstoreapp.data.Category
import com.example.bookstoreapp.data.Task
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.QueryMap

class ApiRepository {
    private val apiService = RetrofitInstance.apiService

    suspend fun getTasks(
        options: Map<String, String>,
        idToken: String
    ): List<Task> {
        for(option in options) Log.d("Option", option.key + " " + option.value)
        return apiService.getTasks(options, idToken)
    }
    
    suspend fun createTask(
        task: Task,
        idToken: String
    ): Int {
        return apiService.createTask(task, idToken)
    }
    
    suspend fun updateTask(
        task: Task,
        idToken: String
    ) {
        return apiService.updateTask(task, idToken)
    }
    
    suspend fun deleteTask(
        id: Int,
        idToken: String
    ){
        return apiService.deleteTask(id, idToken)
    }
    
    suspend fun getCategories(
        idToken: String
    ): List<Category> {
        return apiService.getCategories(idToken)
    }
    
    suspend fun createCategory(
        category: Category,
        idToken: String
    ): Int {
        return apiService.createCategory(category, idToken)
    }
    
    suspend fun updateCategory(
        category: Category,
        idToken: String
    ) {
        return apiService.updateCategory(category, idToken)
    }
    
    suspend fun deleteCategory(
        id: Short,
        idToken: String
    ) {
        return apiService.deleteCategory(id, idToken)
    }
}