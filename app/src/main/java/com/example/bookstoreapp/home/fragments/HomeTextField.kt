package com.example.bookstoreapp.home.fragments

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.bookstoreapp.AppUtils.showToast

@Composable
fun HomeTextField(
    text: String,
    label: String,
    singleLine: Boolean,
    maxLength: Int = 25,
    onValueChange: (String) -> Unit
){
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    var tfText by remember { mutableStateOf(text) }
    TextField(
        value = tfText,
        onValueChange = {
            if (it.length <= maxLength) {
                tfText = it
                onValueChange(it)
            } else {
                showToast(context, "$label max length is $maxLength chars")
            }

        },
        maxLines = 4, // only visible, not actual
        minLines = 4,
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            cursorColor = Color.Black,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        ),
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
            .padding(),
        label = {
            Text(text = label, color = Color.Black)
        },
        singleLine = singleLine,
    )
}