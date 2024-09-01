package com.example.bookstoreapp.login

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector4D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.atomic.AtomicBoolean

class EffectsLauncher(
    private var color1: Color,
    private var color2: Color,
    private var animated: Animatable<Color, AnimationVector4D>
) {
    private var switch = AtomicBoolean(true)
    private var isLaunched = AtomicBoolean(false)

    @Composable
    fun LaunchEffects(){
        if (isLaunched.get()) return

        isLaunched.set(true)
        switch.set(true)

        LaunchedEffect(switch) {
            while (switch.get()) {
                Log.d("ASYNC", switch.get().toString())
                animated.animateTo(color1, animationSpec = tween(4000, 3000))
                animated.animateTo(color2, animationSpec = tween(4000, 3000))
            }
            isLaunched.set(false)
        }
    }

    fun turnOff(){
        switch.set(false)
        Log.d("ASYNC", switch.get().toString())
    }

    fun changeColors(
        color1: Color,
        color2: Color
    ) {
        this.color1 = color1
        this.color2 = color2
    }

}