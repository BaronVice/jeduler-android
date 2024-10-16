package com.example.bookstoreapp.retrofit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstoreapp.data.Category
import com.example.bookstoreapp.data.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class ApiViewModel : ViewModel() {
    private val repository = ApiRepository()
    private val _categories = MutableLiveData<List<Category>>()
    private val _tasks = MutableLiveData<List<Task>>()
    private var _idToken: String = "-1"

    val categories: LiveData<List<Category>> = _categories
    val tasks: LiveData<List<Task>> = _tasks

    fun fetchToken(){
        val tokenTask = Firebase.auth.currentUser?.getIdToken(true)
        tokenTask?.addOnSuccessListener {
            _idToken = tokenTask.result?.token.toString()
        }
    }

    fun fetchCategories() {
        Log.d("Token", _idToken)
        viewModelScope.launch {
            try {
                val c = repository.getCategories(_idToken)
                _categories.value = c
            } catch (e: Exception){
                Log.d("ApiRequestException", e.message?:"")
            }
        }
    }

    fun fetchTasks(
        options: Map<String, String>
    ) {
        Log.d("Token", _idToken)
        viewModelScope.launch {
            try {
                val t = repository.getTasks(options, _idToken)
                _tasks.value = t
            } catch (e: Exception){
                Log.d("ApiRequestException", e.message?:"")
            }
        }
    }

    fun createTask(
        task: Task
    ) {
        Log.d("Token", _idToken)
        viewModelScope.launch {
            try {
                repository.createTask(task, _idToken)
                // todo: fetch tasks?
            } catch (e: Exception){
                Log.d("ApiRequestException", e.message?:"")
            }
        }
    }

    fun updateTask(
        task: Task
    ) {
        Log.d("Token", _idToken)
        viewModelScope.launch {
            try {
                repository.updateTask(task, _idToken)
                // todo: fetch tasks?
            } catch (e: Exception){
                Log.d("ApiRequestException", e.message?:"")
            }
        }
    }

    fun deleteTask(
        id: Int
    ) {
        viewModelScope.launch {
            try {
                repository.deleteTask(id, _idToken)
                // todo: fetch tasks?
            } catch (e: Exception){
                Log.d("ApiRequestException", e.message?:"")
            }
        }
    }

    fun createCategory(
        category: Category
    ) {
        Log.d("Token", _idToken)
        viewModelScope.launch {
            try {
                repository.createCategory(category, _idToken)
                // todo: fetch tasks?
            } catch (e: Exception){
                Log.d("ApiRequestException", e.message?:"")
            }
        }
    }

    fun updateCategory(
        category: Category
    ) {
        Log.d("Token", _idToken)
        viewModelScope.launch {
            try {
                repository.updateCategory(category, _idToken)
                // todo: fetch tasks?
            } catch (e: Exception){
                Log.d("ApiRequestException", e.message?:"")
            }
        }
    }

    fun deleteCategory(
        id: Short
    ) {
        viewModelScope.launch {
            try {
                repository.deleteCategory(id, _idToken)
                // todo: fetch tasks?
            } catch (e: Exception){
                Log.d("ApiRequestException", e.message?:"")
            }
        }
    }
}