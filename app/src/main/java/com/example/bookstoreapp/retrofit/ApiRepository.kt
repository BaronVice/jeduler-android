package com.example.bookstoreapp.retrofit

import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.bookstoreapp.data.Category
import com.example.bookstoreapp.data.Task
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiRepository {
    private val apiService = RetrofitInstance.apiService
    private var categoryLock = true
    private var taskLock = true

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
    
    fun createCategory(
        category: Category,
        categories: SnapshotStateList<Category>,
        idToken: String
    ) {
        val call = apiService.createCategory(category, idToken)
        call.enqueue(
            object : Callback<Short> {
                override fun onResponse(call: Call<Short>, response: Response<Short>) {
                    if (response.isSuccessful) {

                        val id = response.body()
//                        category.id = id!!
                        val index = categories.indexOfFirst { c -> c.id == category.id }
                        categories[index] = Category(
                            id!!,
                            category.name,
                            category.color
                        )
                        Log.d("CategoryCreate", id.toString())
                    }
                }

                override fun onFailure(call: Call<Short>, t: Throwable) {
                    Log.d("CategoryCreate", "Failed")
                }
            }
        )
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