package com.example.bookstoreapp.retrofit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstoreapp.data.Category
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class ApiViewModel : ViewModel() {
    private val repository = ApiRepository()
    private val _categories = MutableLiveData<List<Category>>()
    private var _idToken: String = "-1"

    val categories: LiveData<List<Category>> = _categories

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
}