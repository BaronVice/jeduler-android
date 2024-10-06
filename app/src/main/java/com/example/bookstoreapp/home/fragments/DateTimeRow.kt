package com.example.bookstoreapp.home.fragments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookstoreapp.home.tasks.taskview.DatePickerModal
import com.example.bookstoreapp.home.tasks.taskview.TimePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimeRow(
    text: String,
    cardTime: MutableState<String>,
    cardDate: MutableState<String>
){
    val timeFormatter = remember { SimpleDateFormat("HH:mm", Locale.UK) }
    val dateFormater = remember { SimpleDateFormat("dd.MM.yyyy", Locale.UK) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp, 10.dp, 10.dp, 10.dp),
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

                    cardTime.value = timeFormatter.format(cal.time)
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

                    cardDate.value = dateFormater.format(
                        Date(selectedDate!!.selectedDateMillis!!)
                    )
                    showDatePicker = false
                }
            )
        }


        Text(
            text = text,
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
                text = cardTime.value,
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
                text = cardDate.value,
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