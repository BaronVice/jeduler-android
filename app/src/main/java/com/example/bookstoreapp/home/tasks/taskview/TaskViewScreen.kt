package com.example.bookstoreapp.home.tasks.taskview

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookstoreapp.AppUtils.defaultDate
import com.example.bookstoreapp.AppUtils.defaultTime
import com.example.bookstoreapp.AppUtils.getDateFromTask
import com.example.bookstoreapp.AppUtils.getTimeFromTask
import com.example.bookstoreapp.AppUtils.showToast
import com.example.bookstoreapp.data.Task
import com.example.bookstoreapp.home.fragments.CategoryCard
import com.example.bookstoreapp.home.fragments.DateTimeRow
import com.example.bookstoreapp.home.fragments.HomeButton
import com.example.bookstoreapp.home.fragments.HomeText
import com.example.bookstoreapp.home.fragments.HomeTextField
import com.example.bookstoreapp.home.tasks.taskview.subtasksview.SubtasksCarrier
import com.example.bookstoreapp.home.tasks.taskview.subtasksview.SubtasksView
import com.example.bookstoreapp.retrofit.ApiViewModel
import com.example.bookstoreapp.ui.theme.HighPriority
import com.example.bookstoreapp.ui.theme.LowPriority
import com.example.bookstoreapp.ui.theme.MediumPriority
import com.example.bookstoreapp.ui.theme.PriorityChosenBorderColor
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskViewScreen(
    api: ApiViewModel,
    originalTask: Task,
    firstButtonText: String,
    firstButtonAction: (Task) -> Unit,
    navBack: () -> Unit
){
    val context = LocalContext.current
    val timeFormatter = remember { SimpleDateFormat("HH:mm", Locale.UK) }
    val dateFormater = remember { SimpleDateFormat("dd.MM.yyyy", Locale.UK) }
    val task by remember { mutableStateOf(originalTask.copy()) }

    val categories by api.categories.observeAsState(emptyList())
    LaunchedEffect(Unit) {
        api.fetchCategories()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ){
        Column{
            val cardTime = remember { mutableStateOf(
                if (task.startsAt != "") getTimeFromTask(task.startsAt)
                else timeFormatter.format(defaultTime().time)
            ) }
            val cardDate = remember { mutableStateOf(
                if (task.startsAt != "") getDateFromTask(task.startsAt)
                else dateFormater.format(defaultDate().time)
            ) }
            val subtasks = remember { task.subtasks.toMutableStateList() }

            val chosen = remember { categories.filter { c -> c.id in task.categoryIds }.toMutableStateList() }
            val available = remember { categories.filterNot { c -> c.id in task.categoryIds }.toMutableStateList() }

            var isRevealed by remember { mutableStateOf(false) }
            val scope = rememberCoroutineScope()

            val contextMenuWidth = remember {
                mutableFloatStateOf(0f)
            }
            val offset = remember {
                Animatable(initialValue = 0f)
            }

            SwipeableItemWithActions(
                isRevealed = isRevealed,
                contextMenuWidth = contextMenuWidth,
                offset = offset,
                modifier = Modifier.weight(0.75f),
                swipedContent = {
                    SubtasksView(
                        task = task,
                        SubtasksCarrier(
                            subtasks
                        )
                    )
                }
            ) {
                Column{
                    HomeTextField(
                        text = task.name,
                        "Name",
                        true,
                    ){
                        task.name = it
                    }
                    HomeTextField(
                        text = task.description,
                        "Description",
                        false,
                        maxLength = 150,
                    ){
                        task.description = it
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val borderColors = listOf(
                            remember { mutableStateOf(Color.Black) },
                            remember { mutableStateOf(Color.Black) },
                            remember { mutableStateOf(Color.Black) }
                        )
                        borderColors[task.priority-1].value = PriorityChosenBorderColor

                        PriorityButton(
                            text = "Low",
                            color = LowPriority,
                            borderColors[2].value,
                        ) {
                            task.priority = 3
                            updateBorderColors(borderColors, task.priority)
                        }
                        PriorityButton(
                            text = "Medium",
                            color = MediumPriority,
                            borderColors[1].value,
                        ) {
                            task.priority = 2
                            updateBorderColors(borderColors, task.priority)
                        }
                        PriorityButton(
                            text = "High",
                            color = HighPriority,
                            borderColors[0].value,
                        ) {
                            task.priority = 1
                            updateBorderColors(borderColors, task.priority)
                        }
                    }

                    HomeText(
                        text = "Chosen categories",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp, 8.dp, 8.dp, 0.dp)
                    )

                    LazyRow(
                        modifier = Modifier
                            .height(60.dp)
                            .padding(horizontal = 6.dp)
                    ) {
                        items(chosen, key = {c -> c.id}){
                            category ->
                            CategoryCard(category) {
                                chosen.removeIf { c -> c.id == category.id }
                                available.add(category)
                            }
                        }
                    }
                    HomeText(
                        text = "Available categories",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp, 0.dp, 8.dp, 0.dp)
                    )
                    LazyRow(
                        modifier = Modifier
                            .height(60.dp)
                            .padding(horizontal = 6.dp)
                    ) {
                        items(available, key = {c -> c.id}) { category ->
                            CategoryCard(category) {
                                available.removeIf { c -> c.id == category.id }
//                                task.categoryIds.add(category.id)
                                chosen.add(category)
                            }
                        }
                    }

                    DateTimeRow(text = "Starts at", cardTime = cardTime, cardDate = cardDate)
                }
            }

            Column{
                Surface(
                    modifier = Modifier
                        .pointerInput(contextMenuWidth) {
                            detectHorizontalDragGestures(
                                onHorizontalDrag = { _, dragAmount ->
                                    scope.launch {
                                        val newOffset = (offset.value + dragAmount)
                                            .coerceIn(0f, contextMenuWidth.floatValue)
                                        offset.snapTo(newOffset)
                                    }
                                },
                                onDragEnd = {
                                    when {
                                        offset.value >= contextMenuWidth.floatValue / 2f -> {
                                            scope.launch {
                                                offset.animateTo(contextMenuWidth.floatValue)
                                                isRevealed = true
                                            }
                                        }

                                        else -> {
                                            scope.launch {
                                                offset.animateTo(0f)
                                                isRevealed = false
                                            }
                                        }
                                    }
                                }
                            )
                        }
                ){
                    Text(
                        text = if (!isRevealed) "Swipe me right to see subtasks"
                        else "Swipe me left to see task",
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        fontSize = 25.sp,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Light,
                        modifier = Modifier
                            .padding(vertical = 25.dp, horizontal = 10.dp)
                            .fillMaxWidth()
                    )
                }
                HomeButton(text = firstButtonText) {
                    if (task.name == ""){
                        showToast(context, "Task name cannot be empty")
                        return@HomeButton
                    } else {
                        task.startsAt = "${cardDate.value}+${cardTime.value}"
                        task.subtasks = subtasks.toList()
                        task.categoryIds = chosen.map { c -> c.id }.toList()

                        firstButtonAction(task)
                    }
                }
                HomeButton(text = "To tasks") {
                    navBack()
                }
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
    if (priority != 0.toShort()) colors[priority-1].value = PriorityChosenBorderColor
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
            ),
    ) {
        Text(
            text = text,
            color = Color.Black,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 15.dp)
        )
    }
}