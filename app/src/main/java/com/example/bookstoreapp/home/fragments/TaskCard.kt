package com.example.bookstoreapp.home.fragments

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookstoreapp.AppUtils.parseTaskDate
import com.example.bookstoreapp.data.Task
import com.example.bookstoreapp.ui.theme.HighPriority
import com.example.bookstoreapp.ui.theme.LowPriority
import com.example.bookstoreapp.ui.theme.MediumPriority
import kotlinx.coroutines.delay

@Composable
fun TaskCard(
    task: Task,
    onClick: () -> Unit
) {
    Log.d("TASK_CARD", task.startsAt)
    OutlinedCard(
        onClick = { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = when (task.priority.toInt()) {
                1 -> HighPriority
                2 -> MediumPriority
                else -> {
                    LowPriority
                }
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Column {
            Text(
                text = task.name,
                color = Color.Black,
                fontSize = 25.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Light,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 10.dp, 10.dp, 0.dp)
            )
            Text(
                text = parseTaskDate(task.startsAt),
                color = Color.Black,
                fontSize = 15.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Light,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(8.dp, 7.dp, 10.dp, 6.dp)

            ) {
                Checkbox(
                    checked = task.taskDone,
                    onCheckedChange = {},
                    enabled = false
                )
                Text(
                    text = if (task.taskDone) "Completed" else "Unfinished",
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Light,
                )
            }
        }
    }
}

@Composable
fun <T> SwipeToDeleteContainer(
    item: T,
    animationDuration: Int = 500,
    onDelete: (T) -> Unit,
    content: @Composable (T) -> Unit
){
    var isRemoved by remember { mutableStateOf(false) }
    val state = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            value -> if (value == SwipeToDismissBoxValue.EndToStart){
                isRemoved = true
                true
            } else {
                false
            }
        }
    )

    LaunchedEffect(isRemoved) {
        if (isRemoved){
            delay(animationDuration.toLong())
            onDelete(item)
        }
    }
    
    AnimatedVisibility(
        visible = !isRemoved,
        exit = shrinkVertically(
            animationSpec = tween(animationDuration),
            shrinkTowards = Alignment.Top
        ) + fadeOut()
    ) {
        SwipeToDismissBox(
            state = state,
            backgroundContent = { DeleteBackground(state) },
            enableDismissFromStartToEnd = false,
            content = {
                content(item)
            }
        )

    }
}

@Composable
fun DeleteBackground(
    swipeDismissState: SwipeToDismissBoxState
){
    val color = if (swipeDismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart){
        Color.Red
    } else Color.Transparent

    Box(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxSize()
            .background(
                color,
                RoundedCornerShape(8.dp)
            )
            .padding(5.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            Icons.Default.Delete,
            contentDescription = "delete",
            tint = Color.White
        )
    }
}