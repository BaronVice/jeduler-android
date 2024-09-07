package com.example.bookstoreapp.login

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.bookstoreapp.ui.theme.BorderColor

@Composable
fun RoundedCornerTextField(
    text: String,
    label: String,
    passwordTransformation: Boolean = false,
    onValueChange: (String) -> Unit
){
    TextField(
        value = text,
        onValueChange = { onValueChange(it) },
        shape = RoundedCornerShape(25.dp),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            cursorColor = BorderColor,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
        modifier = Modifier.fillMaxWidth().border(1.dp, BorderColor, RoundedCornerShape(25.dp)),
        label = {
            Text(text = label, color = BorderColor)
        },
        singleLine = true,
        visualTransformation = if (passwordTransformation) PasswordVisualTransformation() else VisualTransformation.None
    )
}