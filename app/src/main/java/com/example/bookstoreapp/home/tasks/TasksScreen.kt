package com.example.bookstoreapp.home.tasks

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookstoreapp.AppUtils.dateNow
import com.example.bookstoreapp.AppUtils.getContrastColor
import com.example.bookstoreapp.AppUtils.hexToColor
import com.example.bookstoreapp.data.Category
import com.example.bookstoreapp.data.Task
import com.example.bookstoreapp.home.fragments.HomeText
import com.example.bookstoreapp.home.fragments.SwipeToDeleteContainer
import com.example.bookstoreapp.home.fragments.TaskCard
import com.example.bookstoreapp.retrofit.ApiViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun TasksScreen(
    api: ApiViewModel,
    onTaskClick: (Int) -> Unit,
    onCategoriesClick: () -> Unit
) {
    val categories by api.categories.observeAsState(emptyList())
    val tasks by api.tasks.observeAsState(emptyList())
    val timeFormatter = remember { SimpleDateFormat("dd.MM.yyyy-HH:mm", Locale.UK) }


    LaunchedEffect(Unit) {
        api.fetchCategories()
        api.fetchTasks(
            mapOf(
                Pair("from", timeFormatter.format(dateNow().time)),
                Pair("order", "starts")
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp, vertical = 20.dp),
    ) {
        LaunchedEffect(key1 = tasks.size) {
            if (tasks.isEmpty()){
//                Log.d("APP_REQUESTS", "Send request")
//                tasks.addAll(getTasks("").toMutableStateList())
                api.fetchTasks(
                    mapOf(
                        Pair("from", timeFormatter.format(dateNow().time)),
                        Pair("order", "starts")
                    )
                )
            }
        }

        Column {
            if (categories.isEmpty()){
                HomeText(
                    text = "Loading...",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .padding(20.dp),
                    size = 20.sp,
                )
            } else {
                LazyRow(
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onCategoriesClick()
                    }
                ) {
                    items(categories){
                        category -> CategoryHolder(category = category)
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Upcoming tasks",
                color = Color.Black,
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = 15.dp,
                        horizontal = 4.dp
                    )
            )
            if (tasks.isEmpty()){
                HomeText(
                    text = "Loading...",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .padding(20.dp),
                    size = 20.sp,
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .padding(0.dp, 0.dp, 0.dp, 65.dp)
                ) {
                    items(tasks, key = {t -> t.id!!}) { task ->
                        SwipeToDeleteContainer(
                            item = task,
                            onDelete = {
                                api.deleteTask(task.id!!)
//                                tasks.remove(task)
                            }
                        ) { t ->
                            Log.d("TaskTime", t.startsAt)
                            TaskCard(task = t) {
                                onTaskClick(t.id!!)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryHolder(
    category: Category
){
    Box(
        modifier = Modifier
            .padding(5.dp)
            .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
    ){
        Surface(
            shadowElevation = 8.dp,
            shape = RoundedCornerShape(8.dp),
            color = Color(hexToColor(category.color))
        ){
            HomeText(
                text = category.name,
                color = getContrastColor(category.color),
                modifier = Modifier
                    .padding(10.dp)
                    .padding(20.dp),
                size = 20.sp,
            )
        }
    }
}