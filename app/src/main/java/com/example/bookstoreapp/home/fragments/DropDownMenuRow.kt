package com.example.bookstoreapp.home.fragments

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@Composable
fun DropDownMenuRow(
    title: String,
    menuItems: List<String>,
    value: MutableState<String>
){
    var expanded by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(10.dp)
    ){
        HomeText(text = title)
        Spacer(modifier = Modifier.padding(vertical = 0.dp, horizontal = 5.dp))
        HomeText(
            text = value.value,
            modifier = Modifier.clickable {
                expanded = true
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            offset = DpOffset(x = 120.dp, y = 0.dp)
        ) {
            for (item in menuItems){
                DropdownMenuItem(
                    onClick = { value.value = item; expanded = false },
                    text = { HomeText(item) }
                )
            }
        }
    }
}
