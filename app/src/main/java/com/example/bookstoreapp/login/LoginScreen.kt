package com.example.bookstoreapp.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.bookstoreapp.AppUtils.showToast
import com.example.bookstoreapp.R
import com.example.bookstoreapp.login.google.SignInState


// TODO: add buttons to change backgrounds
@Composable
fun LoginScreen(
    state: SignInState,
    enable: MutableState<Boolean>,
    onSignInClick: () -> Unit
) {
    val effectsViewModelBg = EffectsViewModel()
    effectsViewModelBg.LaunchEffects()

    val context = LocalContext.current
    LaunchedEffect(key1 = state.signInError) {
        enable.value = true
        state.signInError?.let {
            error -> showToast(context, error)
        }
    }

    Image(
        painter = painterResource(id = R.drawable.login_bg),
        contentDescription = "LOGIN_BG",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(effectsViewModelBg.value),
        contentAlignment = Alignment.Center
    ){
        LoginButton(
            "Sign In",
            enable.value
        ) {
            onSignInClick()
        }
    }
}
