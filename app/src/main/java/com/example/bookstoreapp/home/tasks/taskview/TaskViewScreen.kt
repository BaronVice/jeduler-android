package com.example.bookstoreapp.home.tasks.taskview

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookstoreapp.AppUtils.getContrastColor
import com.example.bookstoreapp.data.Category
import com.example.bookstoreapp.data.Task
import com.example.bookstoreapp.home.HomeButton
import com.example.bookstoreapp.home.TaskCard
import com.example.bookstoreapp.ui.theme.HighPriority
import com.example.bookstoreapp.ui.theme.LowPriority
import com.example.bookstoreapp.ui.theme.MediumPriority
import com.example.bookstoreapp.ui.theme.PriorityChosenBorderColor

@Composable
fun TaskViewScreen(
    tasks: SnapshotStateList<Task>,
    index: Int,
    navBack: () -> Unit
){
    val bgColor = remember { mutableStateOf(Color.White) }
    val task = tasks[index]

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .background(bgColor.value),
        contentAlignment = Alignment.Center
    ){
        val borderColors = listOf(
            remember { mutableStateOf(Color.Black) },
            remember { mutableStateOf(Color.Black) },
            remember { mutableStateOf(Color.Black) }
        )
        borderColors[task.priority-1].value = PriorityChosenBorderColor

        Column{
            TaskViewTextField(
                text = task.name,
                "Name",
                true
            ){
                task.name = it
            }
            TaskViewTextField(
                text = task.description,
                "Description",
                false
            ){
                task.description = it
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PriorityButton(
                    text = "Low",
                    color = LowPriority,
                    borderColors[0].value
                ) {
                    task.priority = 3
                    updateBorderColors(borderColors, task.priority)
                }
                PriorityButton(
                    text = "Medium",
                    color = MediumPriority,
                    borderColors[1].value
                ) {
                    task.priority = 2
                    updateBorderColors(borderColors, task.priority)
                }
                PriorityButton(
                    text = "High",
                    color = HighPriority,
                    borderColors[2].value
                ) {
                    task.priority = 1
                    updateBorderColors(borderColors, task.priority)
                }
            }

            HomeButton(text = "To tasks") {
                navBack()
            }
        }
    }
}

fun updateBorderColors(
    colors: List<MutableState<Color>>,
    priority: Short
){
    for (c in colors){
        c.value = Color.Black
    }
    colors[priority-1].value = PriorityChosenBorderColor
}

@Composable
fun TaskViewTextField(
    text: String,
    label: String,
    singleLine: Boolean,
    onValueChange: (String) -> Unit
){
    val focusManager = LocalFocusManager.current
    var tfText by remember { mutableStateOf(text) }
    TextField(
        value = tfText,
        onValueChange = {
            tfText = it
            onValueChange(it)
        },
        maxLines = 4, // TODO: only visible, not actual
//        minLines = 4,
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            cursorColor = Color.Black,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        ),
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
            .padding(),
        label = {
            Text(text = label, color = Color.Black)
        },
        singleLine = singleLine,
    )
}

@Composable
fun PriorityButton(
    text: String,
    color: Color,
    borderColor: Color,
    onClick: () -> Unit
){
    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Text(
            text = text,
            color = Color.Black,
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 15.dp)
        )
    }
}