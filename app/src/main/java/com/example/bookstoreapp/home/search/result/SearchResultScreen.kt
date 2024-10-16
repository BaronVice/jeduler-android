package com.example.bookstoreapp.home.search.result

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookstoreapp.data.Task
import com.example.bookstoreapp.home.fragments.HomeButton
import com.example.bookstoreapp.home.fragments.SwipeToDeleteContainer
import com.example.bookstoreapp.home.fragments.TaskCard
import com.example.bookstoreapp.home.tasks.CategoryHolder
import com.example.bookstoreapp.home.tasks.taskview.TaskView

@Composable
fun SearchResultScreen(
    // todo: api + map of options
    url: String,
    onNavBack: () -> Unit,
    onTaskClick: (Int) -> Unit
){
    val tasks = remember {
        /*getTasks(url)*/emptyList<Task>().toMutableStateList()
    }

    Log.d("APP_REQUESTS", url)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp, vertical = 20.dp),
    ) {
        LaunchedEffect(key1 = tasks.size) {
            if (tasks.size == 0){
                var a = 0
                for (i in 1..100000000) {
                    a += i
                }
                a += 1
                Log.d("APP_REQUESTS", "Send request")
//                tasks.addAll(getTasks(url))
            }
        }

        Column {
            LazyColumn(
                modifier = Modifier
                    .padding(0.dp, 0.dp, 0.dp, 10.dp)
                    .weight(0.8f)
            ) {
                items(tasks){
                    task ->
                    // TODO: still buggy, have to replace with key in items
                    val movableContent = movableContentOf {
                        SwipeToDeleteContainer(
                            item = task,
                            onDelete = {
                                tasks.remove(task)
                            }
                        ) {
                            t -> TaskCard(task = t) {
                                onTaskClick(t.id!!)
                            }
                        }
                    }

                    movableContent()
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ){
                // TODO: disable if page 0
                HomeButton(text = "<", fillMaxWidth = false) {
                    // prev page
                }
                HomeButton(text = "Reset", fillMaxWidth = false) {
                    onNavBack()
                }
                // TODO: I can ask 11 tasks but show 10, if less than 11 received -> disable
                HomeButton(text = ">", fillMaxWidth = false) {
                    // next page
                }
            }
        }
    }
}