package com.example.bookstoreapp.home.fragments

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookstoreapp.AppUtils.parseTaskDate
import com.example.bookstoreapp.data.Task
import com.example.bookstoreapp.ui.theme.HighPriority
import com.example.bookstoreapp.ui.theme.LowPriority
import com.example.bookstoreapp.ui.theme.MediumPriority

@Composable
fun TaskCard(
    task: Task,
    onClick: () -> Unit
) {
    Log.d("TASK_CARD", task.startsAt)
    OutlinedCard(
        onClick = { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = when (task.priority.toInt()) {
                1 -> HighPriority
                2 -> MediumPriority
                else -> {
                    LowPriority
                }
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Column {
            Text(
                text = task.name,
                color = Color.Black,
                fontSize = 25.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Light,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 10.dp, 10.dp, 0.dp)
            )
            Text(
                text = parseTaskDate(task.startsAt),
                color = Color.Black,
                fontSize = 15.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Light,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(8.dp, 7.dp, 10.dp, 6.dp)

            ) {
                Checkbox(
                    checked = task.taskDone,
                    onCheckedChange = {},
                    enabled = false
                )
                Text(
                    text = if (task.taskDone) "Completed" else "Unfinished",
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Light,
                )

            }
        }
    }
}