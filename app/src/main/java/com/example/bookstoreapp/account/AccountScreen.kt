package com.example.bookstoreapp.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.bookstoreapp.R
import com.example.bookstoreapp.login.AuthUtils
import com.example.bookstoreapp.login.EffectsViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun AccountScreen(
    onSignOut: () -> Unit
){
    val effectsViewModelLogo = remember { EffectsViewModel() }
    effectsViewModelLogo.color1 = Color.Green
    effectsViewModelLogo.color2 = Color.Cyan
    Box(
        modifier = Modifier.fillMaxSize(),
    ){
        effectsViewModelLogo.LaunchEffects()
        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Image(
                painter = painterResource(id = R.drawable.auth),
                contentDescription = "LOGO",
                modifier = Modifier.size(130.dp),
                colorFilter = ColorFilter.tint(effectsViewModelLogo.value)
            )
            Spacer(modifier = Modifier.height(50.dp))
            Button(
                onClick = {
                    Firebase.auth.signOut()
                    onSignOut()
                }
            ) {
                Text(text = "Sign out")
            }

        }
    }
}