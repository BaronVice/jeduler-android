package com.example.bookstoreapp.retrofit

import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstoreapp.data.Category
import com.example.bookstoreapp.data.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlin.math.max

class ApiViewModel : ViewModel() {
    private val repository = ApiRepository()
    private val _categories = MutableLiveData<List<Category>>()
    private val _tasks = MutableLiveData<List<Task>>()
    private var _idToken: String = "-1"
    val categories: LiveData<List<Category>> = _categories

    val tasks: LiveData<List<Task>> = _tasks
    private var options: MutableMap<String, String> = mutableMapOf()

    fun changeOptions(
        newOptions: MutableMap<String, String>
    ){
        options = newOptions
    }

    fun previousPage(){
        val page = max(options["page"]!!.toInt() - 1, 0)
        options["page"] = page.toString()
    }

    fun nextPage(){
        val page = options["page"]!!.toInt() + 1
        options["page"] = page.toString()
    }

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

    fun fetchTasks() {
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
        category: Category,
        categories: SnapshotStateList<Category>
    ) {
        Log.d("Token", _idToken)
        viewModelScope.launch {
            try {
                Log.d("ImHere", "1")
                repository.createCategory(category, categories, _idToken)
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