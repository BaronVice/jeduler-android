package com.example.bookstoreapp.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeButton(
    text: String,
    onClick: () -> Unit
){
    Button(
        onClick = {
            onClick()
        },
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black,
            contentColor = Color.White,
        ),
        elevation = ButtonDefaults.elevatedButtonElevation(10.dp),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = text,
            fontSize = 20.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(10.dp)
        )
    }
}