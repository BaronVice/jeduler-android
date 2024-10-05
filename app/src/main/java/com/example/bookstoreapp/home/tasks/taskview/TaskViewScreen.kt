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
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
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
import com.example.bookstoreapp.home.fragments.HomeButton
import com.example.bookstoreapp.home.fragments.HomeTextField
import com.example.bookstoreapp.home.tasks.taskview.subtasksview.SubtasksCarrier
import com.example.bookstoreapp.home.tasks.taskview.subtasksview.SubtasksView
import com.example.bookstoreapp.ui.theme.HighPriority
import com.example.bookstoreapp.ui.theme.LowPriority
import com.example.bookstoreapp.ui.theme.MediumPriority
import com.example.bookstoreapp.ui.theme.PriorityChosenBorderColor
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskViewScreen(
    data: CategoryList,
    originalTask: Task,
    firstButtonText: String,
    firstButtonAction: (Task) -> Unit,
    navBack: () -> Unit
){
    val context = LocalContext.current
    val timeFormatter = remember { SimpleDateFormat("HH:mm", Locale.UK) }
    val dateFormater = remember { SimpleDateFormat("dd.MM.yyyy", Locale.UK) }
    val task by remember { mutableStateOf(originalTask.copy()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ){
        Column{
            var cardTime by remember { mutableStateOf(
                if (task.startsAt != "") getTimeFromTask(task.startsAt)
                else timeFormatter.format(defaultTime().time)
            ) }
            var cardDate by remember { mutableStateOf(
                if (task.startsAt != "") getDateFromTask(task.startsAt)
                else dateFormater.format(defaultDate().time)
            ) }
            var subtasks = remember { task.subtasks.toMutableStateList() }

            val chosen = remember { data.categories.filter { c -> c.id in task.categoryIds }.toMutableStateList() }
            val available = remember { data.categories.filterNot { c -> c.id in task.categoryIds }.toMutableStateList() }

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

                    Text(
                        text = "Chosen categories",
                        color = Color.Black,
                        fontSize = 25.sp,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Light,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp, 8.dp, 8.dp, 0.dp)
                    )

                    LazyRow(
                        modifier = Modifier.height(60.dp).padding(horizontal = 6.dp)
                    ) {
                        items(chosen, key = {c -> c.id}){
                            category ->
                            CategoryCard(category) {
                                chosen.removeIf { c -> c.id == category.id }
//                                task.categoryIds.remove(category.id)
                                available.add(category)
                            }
                        }
                    }
                    Text(
                        text = "Available categories",
                        color = Color.Black,
                        fontSize = 25.sp,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Light,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp, 0.dp, 8.dp, 0.dp)
                    )
                    LazyRow(
                        modifier = Modifier.height(60.dp).padding(horizontal = 6.dp)
                    ) {
                        items(available, key = {c -> c.id}) { category ->
                            CategoryCard(category) {
                                available.removeIf { c -> c.id == category.id }
//                                task.categoryIds.add(category.id)
                                chosen.add(category)
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp, 20.dp, 10.dp, 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        var selectedTime: TimePickerState? by remember { mutableStateOf(null) }
                        var showTimePicker by remember { mutableStateOf(false) }

                        var selectedDate: DatePickerState? by remember { mutableStateOf(null) }
                        var showDatePicker by remember { mutableStateOf(false) }

                        if (showTimePicker){
                            TimePicker(
                                onDismiss = {
                                    showTimePicker = false
                                },
                                onConfirm = {
                                        time ->
                                    selectedTime = time

                                    val cal = Calendar.getInstance()
                                    cal.set(Calendar.HOUR_OF_DAY, selectedTime!!.hour)
                                    cal.set(Calendar.MINUTE, selectedTime!!.minute)
                                    cal.isLenient = false

                                    cardTime = timeFormatter.format(cal.time)
                                    showTimePicker = false
                                }
                            )
                        }
                        if (showDatePicker){
                            DatePickerModal(
                                onDismiss = {
                                    showDatePicker = false
                                },
                                onDateSelected = {
                                        date ->
                                    selectedDate = date

                                    cardDate = dateFormater.format(
                                        Date(selectedDate!!.selectedDateMillis!!)
                                    )
                                    showDatePicker = false
                                }
                            )
                        }


                        Text(
                            text = "Starts at ",
                            color = Color.Black,
                            fontSize = 25.sp,
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Light
                        )

                        OutlinedCard(
                            onClick = {
                                showTimePicker = true
                            },
                            modifier = Modifier.padding(horizontal = 15.dp)
                        ) {
                            Text(
                                text = cardTime,
                                color = Color.Black,
                                fontSize = 20.sp,
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Light,
                                modifier = Modifier
                                    .padding(10.dp)
                            )
                        }
                        OutlinedCard(
                            onClick = {
                                showDatePicker = true
                            },
                            modifier = Modifier.padding(horizontal = 15.dp)
                        ) {
                            Text(
                                text = cardDate,
                                color = Color.Black,
                                fontSize = 20.sp,
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Light,
                                modifier = Modifier
                                    .padding(10.dp)
                            )
                        }
                    }
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
                        task.startsAt = "$cardDate+$cardTime" // todo: check if not past?
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
    colors[priority-1].value = PriorityChosenBorderColor
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