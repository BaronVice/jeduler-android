package com.example.bookstoreapp.home.tasks.taskview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookstoreapp.data.Subtask
import com.example.bookstoreapp.data.Task

@Composable
fun SubtasksView(
    task: Task
){
    Column {
        var subtasks = remember { task.subtasks.toMutableStateList() }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ){
            var isDone by remember { mutableStateOf(task.taskDone) }
            var cbText by remember { mutableStateOf(
                if (isDone) "Completed" else "Unfinished"
            ) }

            Checkbox(
                checked = isDone,
                onCheckedChange = { checked ->
                    isDone = checked
                    if (checked) {
                        cbText = "Completed"
                        subtasks.forEach { s -> s.isCompleted = true }
                    } else {
                        cbText = "Unfinished"
                        subtasks.last().isCompleted = false
                    }
                }
            )
            Text(
                text = cbText,
                color = Color.Black,
                fontSize = 25.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Light
            )
        }

        val draggableItems by remember {
            derivedStateOf { subtasks.size }
        }
        val stateList = rememberLazyListState()

        val dragDropState =
            rememberDragDropState(
                lazyListState = stateList,
                draggableItemsNum = draggableItems,
                onMove = { fromIndex, toIndex ->
                    subtasks = subtasks.apply { add(toIndex, removeAt(fromIndex)) }
                })

        LazyColumn(
            modifier = Modifier.dragContainer(dragDropState),
            state = stateList,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            draggableItems(items = subtasks, dragDropState = dragDropState) { modifier, item ->
                Item(
                    modifier = modifier,
                    subtask = item,
                )
            }

        }
    }
}

@Composable
private fun Item(modifier: Modifier = Modifier, subtask: Subtask) {
    Card(
        modifier = modifier
    ) {
        Text(
            "${subtask.name} ${subtask.isCompleted} ${subtask.orderInList}",
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        )
    }
}