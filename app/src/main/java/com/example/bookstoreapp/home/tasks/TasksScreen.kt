package com.example.bookstoreapp.home.tasks

import androidx.compose.foundation.background
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookstoreapp.AppUtils.getContrastColor
import com.example.bookstoreapp.AppUtils.hexToColor
import com.example.bookstoreapp.data.Category
import com.example.bookstoreapp.data.Task
import com.example.bookstoreapp.ui.theme.BorderColor
import com.example.bookstoreapp.ui.theme.HighPriority
import com.example.bookstoreapp.ui.theme.LowPriority
import com.example.bookstoreapp.ui.theme.MediumPriority
import com.example.bookstoreapp.ui.theme.TaskCardColor

@Composable
fun TasksScreen(
    categories: SnapshotStateList<Category>,
    tasks: SnapshotStateList<Task>,
    onCategoriesClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp, vertical = 20.dp),
    ) {
        Column {
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
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Upcoming tasks:",
                color = Color.Black,
                fontSize = 30.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(
                    vertical = 15.dp,
                    horizontal = 4.dp
                )
            )
            LazyColumn(
                modifier = Modifier
                    .padding(0.dp, 0.dp, 0.dp, 65.dp)
            ) {
                // TODO: request on getting 10 tasks, sort upcoming first
                items(tasks){
                    task -> TaskCard(task = task)
                }
            }
        }
    }
}

@Composable
fun CategoryHolder(
    category: Category
){
    Text(
        text = category.name,
        color = getContrastColor(category.color),
        modifier = Modifier
            .padding(10.dp)
            .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
            .background(
                Color(hexToColor(category.color)),
                RoundedCornerShape(8.dp)
            )
            .padding(20.dp),
        fontSize = 20.sp,
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
    )
}

@Composable
fun TaskCard(
    task: Task
){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
                .background(
                    TaskCardColor,
                    RoundedCornerShape(8.dp)
                )
        ) {
            Surface(
                shadowElevation = 8.dp,
                shape = RoundedCornerShape(8.dp)
            ) {
                Column {
                    Text(
                        text = task.name,
                        color = Color.Black,
                        fontSize = 25.sp,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                when (task.priority.toInt()) {
                                    1 -> HighPriority
                                    2 -> MediumPriority
                                    else -> {
                                        LowPriority
                                    }
                                },
                                RoundedCornerShape(8.dp, 8.dp, 0.dp, 0.dp)
                            )
                            .padding(
                                vertical = 10.dp,
                                horizontal = 20.dp
                            )
                    )
                    Spacer(modifier = Modifier.height(50.dp))
                }
            }
        }
}