package com.example.bookstoreapp.login

import androidx.compose.animation.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.bookstoreapp.AppUtils.showToast
import com.example.bookstoreapp.R
import com.example.bookstoreapp.login.google.SignInState
import com.example.bookstoreapp.ui.theme.LoginBoxFilter1
import com.example.bookstoreapp.ui.theme.LogoFilter1
import com.example.bookstoreapp.ui.theme.LogoFilter2
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference


// TODO: add buttons to change backgrounds
@Composable
fun LoginScreen(
    state: SignInState,
    onSignInClick: () -> Unit
) {
    val effectsViewModelBg = EffectsViewModel()
    effectsViewModelBg.LaunchEffects()

    val context = LocalContext.current
    LaunchedEffect(key1 = state.signInError) {
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
        LoginButton(text = "Sign In") {
            onSignInClick()
        }
    }
}
