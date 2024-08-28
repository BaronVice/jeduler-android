package com.example.bookstoreapp.login

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun LoginScreen(current: Context) {
    val auth = Firebase.auth

    val emailState = remember {
        mutableStateOf("")
    }
    val passwordState = remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = emailState.value,
            onValueChange = {
                emailState.value = it
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            value = passwordState.value,
            onValueChange = {
                passwordState.value = it
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = {
                AuthUtils.signIn(auth, emailState.value, passwordState.value, current)
            }
        ) {
            Text(text = "Sign In")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = {
                AuthUtils.signUp(auth, emailState.value, passwordState.value, current)
            }
        ) {
            Text(text = "Sign Up")
        }
    }
}