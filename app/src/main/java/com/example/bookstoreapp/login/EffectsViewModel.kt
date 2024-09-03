package com.example.bookstoreapp.login

import android.util.Log
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector4D
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.bookstoreapp.ui.theme.LoginBoxFilter1
import com.example.bookstoreapp.ui.theme.LoginBoxFilter2
import com.example.bookstoreapp.ui.theme.LogoFilter1

class EffectsViewModel : ViewModel() {
    var color1 by mutableStateOf(LoginBoxFilter1)
    var color2 by mutableStateOf(LoginBoxFilter2)
    var value by mutableStateOf(LoginBoxFilter1)
        private set

    @Composable
    fun LaunchEffects(){
        val animated = remember {
            Animatable(color1)
        }
        value = animated.value

        LaunchedEffect(Unit) {
            while (true) {
                animated.animateTo(color2, animationSpec = tween(4000, 3000))
                animated.animateTo(color1, animationSpec = tween(4000, 3000))
            }
        }
    }

    fun changeColors(
        color1: Color,
        color2: Color
    ) {
        this.color1 = color1
        this.color2 = color2
    }
}
