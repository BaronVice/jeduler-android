package com.example.bookstoreapp.home.search

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookstoreapp.AppUtils.dateNow
import com.example.bookstoreapp.AppUtils.defaultDate
import com.example.bookstoreapp.AppUtils.defaultTime
import com.example.bookstoreapp.AppUtils.getDateFromTask
import com.example.bookstoreapp.AppUtils.getTimeFromTask
import com.example.bookstoreapp.AppUtils.showToast
import com.example.bookstoreapp.AppUtils.timeNow
import com.example.bookstoreapp.RequestsUtils.buildSearchRequest
import com.example.bookstoreapp.data.Category
import com.example.bookstoreapp.data.Task
import com.example.bookstoreapp.home.fragments.CategoryCard
import com.example.bookstoreapp.home.fragments.DateTimeRow
import com.example.bookstoreapp.home.fragments.DropDownMenuRow
import com.example.bookstoreapp.home.fragments.HomeButton
import com.example.bookstoreapp.home.fragments.HomeText
import com.example.bookstoreapp.home.fragments.HomeTextField
import com.example.bookstoreapp.home.tasks.taskview.CategoryList
import com.example.bookstoreapp.home.tasks.taskview.DatePickerModal
import com.example.bookstoreapp.home.tasks.taskview.PriorityButton
import com.example.bookstoreapp.home.tasks.taskview.TimePicker
import com.example.bookstoreapp.home.tasks.taskview.updateBorderColors
import com.example.bookstoreapp.ui.theme.HighPriority
import com.example.bookstoreapp.ui.theme.LowPriority
import com.example.bookstoreapp.ui.theme.MediumPriority
import com.example.bookstoreapp.ui.theme.PriorityChosenBorderColor
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun SearchScreen(
    categoryList: CategoryList,
    onSearch: (String) -> Unit
){
    val context = LocalContext.current
    val timeFormatter = remember { SimpleDateFormat("HH:mm", Locale.UK) }
    val dateFormater = remember { SimpleDateFormat("dd.MM.yyyy", Locale.UK) }

    val task by remember {
        mutableStateOf(
            Task(
                0, "", "", false, 0, listOf(), listOf(), "", ""
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        contentAlignment = Alignment.TopCenter
    ){
        Column {
            val cardTimeStart = remember { mutableStateOf(
                timeFormatter.format(timeNow().time)
            ) }
            val cardDateStart = remember { mutableStateOf(
                dateFormater.format(dateNow().time)
            ) }
            val cardTimeEnd = remember { mutableStateOf(
                timeFormatter.format(timeNow().time)
            ) }
            val cardDateEnd = remember { mutableStateOf(
                dateFormater.format(defaultDate().time)
            ) }

            val chosen = remember { mutableStateListOf<Category>() }
            val available = remember { categoryList.categories.toMutableStateList() }

            val taskDone = remember { mutableStateOf("No") }
            val sortBy = remember { mutableStateOf("Start date") }
            // todo: no order in api sadly
            // val order = remember { mutableStateOf("") }

            val scope = rememberCoroutineScope()

            Column{
                HomeTextField(
                    text = task.name,
                    "Name like:",
                    true,
                ){
                    task.name = it
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

                    PriorityButton(
                        text = "Low",
                        color = LowPriority,
                        borderColors[2].value,
                    ) {
                        task.priority = if (task.priority == 3.toShort()) 0 else 3
                        updateBorderColors(borderColors, task.priority)
                    }
                    PriorityButton(
                        text = "Medium",
                        color = MediumPriority,
                        borderColors[1].value,
                    ) {
                        task.priority = if (task.priority == 2.toShort()) 0 else 2
                        updateBorderColors(borderColors, task.priority)
                    }
                    PriorityButton(
                        text = "High",
                        color = HighPriority,
                        borderColors[0].value,
                    ) {
                        task.priority = if (task.priority == 1.toShort()) 0 else 1
                        updateBorderColors(borderColors, task.priority)
                    }
                }

                HomeText(
                    text = "Includes categories",
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
                            chosen.add(category)
                        }
                    }
                }

                DateTimeRow(text = "From", cardTime = cardTimeStart, cardDate = cardDateStart)
                DateTimeRow(text = "To", cardTime = cardTimeEnd, cardDate = cardDateEnd)

                DropDownMenuRow(
                    title = "Is completed: ",
                    menuItems = listOf("Yes", "No", "Any"),
                    value = taskDone
                )
                DropDownMenuRow(
                    title = "Sort by: ",
                    menuItems = listOf("Start date", "Edit date", "Name", "Priority"),
                    value = sortBy
                )

            }

            Spacer(modifier = Modifier.padding(15.dp))
            HomeButton(text = "Search") {
                // if (datesNotIntersecting)

                task.categoryIds = chosen.map { c -> c.id }.toList()
                val s = buildSearchRequest(
                    name = task.name,
                    priority = task.priority,
                    categoryIds = task.categoryIds,
                    from = "${cardDateStart.value}+${cardTimeStart.value}",
                    to = "${cardDateEnd.value}+${cardTimeEnd.value}",
                    isCompleted = taskDone.value,
                    sortBy = sortBy.value
                )
                onSearch(s)
            }
        }
    }
}