package com.example.bookstoreapp.home.tasks.taskview

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

@Composable
fun SwipeableItemWithActions(
    isRevealed: Boolean,
    contextMenuWidth: MutableState<Float>,
    offset: Animatable<Float, AnimationVector1D>,
    modifier: Modifier = Modifier,
    swipedContent: @Composable () -> Unit,
    mainContent: @Composable () -> Unit
) {
    LaunchedEffect(key1 = isRevealed, contextMenuWidth) {
        if(isRevealed) {
            offset.animateTo(contextMenuWidth.value)
        } else {
            offset.animateTo(0f)
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .onSizeChanged {
                    contextMenuWidth.value = it.width.toFloat()
                },
            contentAlignment = Alignment.Center
        ) {
            swipedContent()
        }
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(offset.value.roundToInt(), 0) }

        ) {
            mainContent()
        }
    }
}