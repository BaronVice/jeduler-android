package com.example.bookstoreapp.home.tasks.taskview

import android.util.Log
import androidx.compose.foundation.border
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
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import com.example.bookstoreapp.AppUtils.defaultDate
import com.example.bookstoreapp.AppUtils.defaultTime
import com.example.bookstoreapp.AppUtils.getDateFromTask
import com.example.bookstoreapp.AppUtils.getTimeFromTask
import com.example.bookstoreapp.data.Task
import com.example.bookstoreapp.home.fragments.CategoryCard
import com.example.bookstoreapp.home.fragments.HomeButton
import com.example.bookstoreapp.home.fragments.HomeTextField
import com.example.bookstoreapp.ui.theme.HighPriority
import com.example.bookstoreapp.ui.theme.LowPriority
import com.example.bookstoreapp.ui.theme.MediumPriority
import com.example.bookstoreapp.ui.theme.PriorityChosenBorderColor
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskViewScreen(
    data: TaskCategoryLists,
    originalTask: Task,
    firstButtonText: String,
    firstButtonAction: () -> Unit,
    navBack: () -> Unit
){
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

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                var isTaskEditable by remember { mutableStateOf(false) }
                Switch(
                    checked = isTaskEditable,
                    onCheckedChange = {
                        isTaskEditable = it
                    },
                    enabled = true,
                    modifier = Modifier.padding(8.dp)
                )
                Text(
                    text = "Editable",
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Light,
                )
            }

            HomeTextField(
                text = task.name,
                "Name",
                true
            ){
                task.name = it
            }
            HomeTextField(
                text = task.description,
                "Description",
                false,
                maxLength = 150
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
                val borderColors = listOf(
                    remember { mutableStateOf(Color.Black) },
                    remember { mutableStateOf(Color.Black) },
                    remember { mutableStateOf(Color.Black) }
                )
                borderColors[task.priority-1].value = PriorityChosenBorderColor

                PriorityButton(
                    text = "Low",
                    color = LowPriority,
                    borderColors[2].value
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
                    borderColors[0].value
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
                    .padding(10.dp, 10.dp, 10.dp, 0.dp)
            )

            val chosen = remember { data.categories.filter { c -> c.id in task.categoryIds }.toMutableStateList() }
            val available = remember { data.categories.filterNot { c -> c.id in task.categoryIds }.toMutableStateList() }
            LazyRow(
                modifier = Modifier.height(60.dp)
            ) {
                items(chosen, key = {c -> c.id}){
                    category -> CategoryCard(category){
                        chosen.removeIf { c -> c.id == category.id }
                        task.categoryIds.remove(category.id)
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
                    .padding(10.dp, 0.dp, 10.dp, 0.dp)
            )
            LazyRow(
                modifier = Modifier.height(60.dp)
            ) {
                items(available, key = {c -> c.id}){
                    category -> CategoryCard(category){
                        available.removeIf { c -> c.id == category.id }
                        task.categoryIds.add(category.id)
                        chosen.add(category)
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                var selectedTime: TimePickerState? by remember { mutableStateOf(null) }
                var showAdvancedExample by remember { mutableStateOf(false) }

                if (showAdvancedExample){
                    TimePicker(
                        onDismiss = {
                            showAdvancedExample = false
                        },
                        onConfirm = {
                            time ->
                            selectedTime = time

                            val cal = Calendar.getInstance()
                            cal.set(Calendar.HOUR_OF_DAY, selectedTime!!.hour)
                            cal.set(Calendar.MINUTE, selectedTime!!.minute)
                            cal.isLenient = false

                            // todo: somehow it break app (found it)
                            cardTime = timeFormatter.format(cal.time)
                            showAdvancedExample = false
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
                        showAdvancedExample = true
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
            }

            HomeButton(text = firstButtonText) {
                task.startsAt = "12.08.2025" + "+" + cardTime
                if (task.equals(originalTask)){
                    Log.d("IS_EQUAL", "Yes")
                } else {
                    Log.d("IS_EQUAL", "No")
                    originalTask.name = task.name
                    originalTask.description = task.description
                    originalTask.priority = task.priority
                    originalTask.categoryIds = task.categoryIds
                    originalTask.subtasks = task.subtasks
                    originalTask.startsAt = task.startsAt
                    originalTask.notifyAt = task.notifyAt
                }
                firstButtonAction()
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
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 15.dp)
        )
    }
}