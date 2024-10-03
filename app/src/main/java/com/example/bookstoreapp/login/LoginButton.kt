package com.example.bookstoreapp.login

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bookstoreapp.ui.theme.BorderColor

@Composable
fun LoginButton(
    text: String,
    enable: Boolean,
    onClick: () -> Unit
){
    Button(
        onClick = { onClick() },
        enabled = enable,
        modifier = Modifier.fillMaxWidth(0.5f),
        colors = ButtonDefaults.buttonColors(
            containerColor = BorderColor
        )
    ) {
        Text(
            text = text,
            Modifier.padding(vertical = 10.dp)
        )
    }
}