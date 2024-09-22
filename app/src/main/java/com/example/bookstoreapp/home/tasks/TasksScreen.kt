package com.example.bookstoreapp.home.tasks

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookstoreapp.AppUtils.getContrastColor
import com.example.bookstoreapp.AppUtils.hexToColor
import com.example.bookstoreapp.data.Category
import com.example.bookstoreapp.home.category.CategoriesScreen

@Composable
fun TasksScreen(
    categories: MutableState<List<Category>>,
    onCategoriesClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 2.dp, vertical = 20.dp),
//        contentAlignment = Alignment.Center
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
                items(categories.value){
                    category -> CategoryHolder(category = category)
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