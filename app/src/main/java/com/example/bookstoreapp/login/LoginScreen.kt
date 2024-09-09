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
import com.example.bookstoreapp.R
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
    onLoginSuccess: () -> Unit
) {
    val auth = Firebase.auth
    val current = LocalContext.current

    val effectsViewModelBg = EffectsViewModel()
    val effectsViewModelLogo = EffectsViewModel()
    effectsViewModelLogo.color2 = LogoFilter1
    effectsViewModelLogo.color1 = LogoFilter2

    effectsViewModelBg.LaunchEffects()
    effectsViewModelLogo.LaunchEffects()

    if (auth.currentUser != null){
        onLoginSuccess()
        return
    }

    val emailState = remember {
        mutableStateOf("")
    }
    val passwordState = remember {
        mutableStateOf("")
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
            .background(effectsViewModelBg.value)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.auth),
            contentDescription = "LOGO",
            modifier = Modifier.size(130.dp),
            colorFilter = ColorFilter.tint(effectsViewModelLogo.value)
        )
        Spacer(modifier = Modifier.height(10.dp))
        // TODO: use some API to get fancy quote
        Spacer(modifier = Modifier.height(30.dp))

        RoundedCornerTextField(
            text = emailState.value,
            label = "Email"
        ){
            emailState.value = it
        }
        Spacer(modifier = Modifier.height(10.dp))
        RoundedCornerTextField(
            text = passwordState.value,
            label = "Password",
            passwordTransformation = true
        ){
            passwordState.value = it
        }
        Spacer(modifier = Modifier.height(100.dp))

        LoginButton(text = "Sign In") {
            if (AuthUtils.signIn(auth, emailState.value, passwordState.value, current)){
                idle()
                onLoginSuccess()
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        LoginButton(text = "Sign Up") {
            if (AuthUtils.signUp(auth, emailState.value, passwordState.value, current)){
                idle()
                onLoginSuccess()
            }
            effectsViewModelLogo.changeColors(Color.Red, Color.Green)
        }
    }
}

fun idle(){
    // TODO: BRUUUUUUUUUUUUUUUUUUUUUUUUUH....................
    //  learn coroutines dammit...
    var a = 1
    for(i in 1..100000000){
        a += a + a
    }
}
