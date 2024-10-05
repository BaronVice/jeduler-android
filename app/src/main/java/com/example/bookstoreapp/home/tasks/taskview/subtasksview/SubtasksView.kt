package com.example.bookstoreapp.home.tasks.taskview.subtasksview

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookstoreapp.AppUtils.getRandomCategoryName
import com.example.bookstoreapp.AppUtils.getRandomHex
import com.example.bookstoreapp.AppUtils.isCategoryNameUnique
import com.example.bookstoreapp.AppUtils.showToast
import com.example.bookstoreapp.data.Category
import com.example.bookstoreapp.data.Subtask
import com.example.bookstoreapp.data.Task
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun SubtasksView(
    task: Task,
    subtasksCarrier: SubtasksCarrier
){
    Column {
        var subtasks = subtasksCarrier.subtasks
        var isDone by remember { mutableStateOf(task.taskDone) }
        var cbText by remember { mutableStateOf(
            if (isDone) "Completed" else "Unfinished"
        ) }

        val context = LocalContext.current
        val corroutineScope = rememberCoroutineScope()
        val stateList = rememberLazyListState()

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.padding(10.dp)
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.weight(0.8f)
            ) {
                Checkbox(
                    checked = isDone,
                    onCheckedChange = { checked ->
                        isDone = checked
                        task.taskDone = checked
                        cbText = if (checked) "Completed" else "Unfinished"
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color.Black,
                        uncheckedColor = Color.Black,
                        checkmarkColor = Color.White
                    )
                )
                Text(
                    text = cbText,
                    color = Color.Black,
                    fontSize = 25.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Light
                )
            }
            OutlinedIconButton(
                onClick = {
                    if (subtasks.size < 10) {
                        subtasks.add(
                            // request on add to get id
                            Subtask(
                                "More work - less fun :<",
                                false,
                                subtasks.size.toShort()
                            )
                        )
                        corroutineScope.launch {
                            stateList.scrollToItem(subtasks.size - 1)
                        }
                    } else {
                        showToast(
                            context,
                            "Oh hey, subtasks limit (10).\nDelete old to add new."
                        )
                    }
                },
                modifier = Modifier.size(50.dp).padding(0.dp)
            ) {
                Icon(Icons.Outlined.Add, null)
            }
        }


        val draggableItems by remember {
            derivedStateOf { subtasks.size }
        }

        val dragDropState =
            rememberDragDropState(
                lazyListState = stateList,
                draggableItemsNum = draggableItems,
                onMove = { fromIndex, toIndex ->
                    subtasks = subtasks.apply { add(toIndex, removeAt(fromIndex)) }
                }
            )

        LazyColumn(
            modifier = Modifier.dragContainer(dragDropState),
            state = stateList,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            draggableItems(
                items = subtasks,
                dragDropState = dragDropState
            ) {
                modifier, subtask ->
                SubtaskCard(
                    modifier = modifier,
                    subtask = subtask,
                    isDone = isDone,
                    lastSubtaskKey = { subtasks.last().orderInList },
                    allDone = { subtasks.all { s -> s.isCompleted } },
                    onDelete = {
                        orderInList -> subtasks.removeIf{ s -> s.orderInList == orderInList }
                    }
                ){
                    checked ->
                    if (checked) {
                        if (subtasks.all { s -> s.isCompleted }) {
                            isDone = true
                            task.taskDone = true
                            cbText = "Completed"
                        }
                    } else {
                        isDone = false
                        task.taskDone = false
                        cbText = "Unfinished"
                    }
                }
            }
        }
    }
}

@Composable
private fun SubtaskCard(
    modifier: Modifier = Modifier,
    subtask: Subtask,
    isDone: Boolean,
    lastSubtaskKey: () -> Short,
    allDone: () -> Boolean,
    onDelete: (Short) -> Boolean,
    onCheckBoxClick: (Boolean) -> Unit
) {
    var isCompleted by remember { mutableStateOf(subtask.isCompleted) }

    var name by remember { mutableStateOf(subtask.name) }
    var cursorColor by remember { mutableStateOf(Color.Black) }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val cardColor = Color.Gray


    LaunchedEffect(key1 = isDone) {
        if (isDone){
            isCompleted = true
            subtask.isCompleted = true
        } else {
            if (subtask.orderInList == lastSubtaskKey() && allDone()){
                isCompleted = false
                subtask.isCompleted = false
            }
        }
    }

    OutlinedCard(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
    ) {
        TextField(
            value = name,
            onValueChange = {
                if (it.length <= 30) {
                    name = it
                    subtask.name = it
                } else {
                    showToast(context, "Max length is 30 chars")
                }
                cursorColor = if (name == "") Color.Transparent else Color.Black
            },
            textStyle = LocalTextStyle.current.copy(
                color = Color.Black,
                fontSize = 20.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Left,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            placeholder = {
                Text(
                    text = "Oh no! Will I be deleted?",
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = cardColor,
                focusedContainerColor = cardColor,
                cursorColor = cursorColor,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
                .onFocusChanged { focusState ->
                    if (!focusState.isFocused) {
                        if (name == "") {
                            onDelete(subtask.orderInList)
                        }
                    }
                },
            singleLine = true
        )

        Checkbox(
            checked = isCompleted,
            onCheckedChange = {
                isCompleted = it
                subtask.isCompleted = it
                onCheckBoxClick(isCompleted)
            },
            colors = CheckboxDefaults.colors(
                checkedColor = Color.Black,
                uncheckedColor = Color.Black,
                checkmarkColor = Color.White
            ),
            modifier = Modifier
                .align(Alignment.End)
        )
    }
}