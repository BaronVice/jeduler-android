package com.example.bookstoreapp.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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

    Box(modifier = )
    Image(
        painter = painterResource(id = R.drawable.auth),
        contentDescription = "LOGO",
        modifier = Modifier.size(130.dp),
        colorFilter = ColorFilter.tint(effectsViewModelLogo.value)
    )
    Button(
        onClick = {
            Firebase.auth.signOut()
            onSignOut()
        }
    ) {
        Text(text = "Sign out")
    }
}