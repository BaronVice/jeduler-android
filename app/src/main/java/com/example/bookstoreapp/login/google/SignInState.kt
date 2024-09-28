package com.example.bookstoreapp.login.google

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)
