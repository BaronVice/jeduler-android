package com.example.bookstoreapp.home.category

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookstoreapp.AppUtils.getContrastColor
import com.example.bookstoreapp.AppUtils.hexToColor
import com.example.bookstoreapp.data.Category
import com.example.bookstoreapp.home.HomeButton
import com.example.bookstoreapp.home.tasks.CategoryHolder
import com.example.bookstoreapp.ui.theme.BorderColor

@Composable
fun CategoriesScreen(
    categories: MutableState<List<Category>>,
    onPopBack: () -> Unit
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 5.dp, vertical = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column{
            Box(
                modifier = Modifier.weight(0.8f)
            ){
                LazyColumn {
                    items(categories.value){
                            category -> CategoryHolderEdit(category = category)
                    }
                }
            }
            Box(
                modifier = Modifier.weight(0.2f)
            ){
                Column {
                    HomeButton(text = "Add category") {
                        // TODO
                    }
                    HomeButton(text = "To tasks") {
                        // TODO: save
                        onPopBack()
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryHolderEdit(
    // TODO: category name and color should be signed as mutable?
    category: Category
){
    Row{
        Box(modifier = Modifier.weight(0.8f)){
            val mainColor = Color(hexToColor(category.color))
            val contrastColor = getContrastColor(category.color)

            TextField(
                value = category.name,
                onValueChange = { category.name = it },
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = mainColor,
                    focusedContainerColor = mainColor,
                    cursorColor = contrastColor,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
        Box(modifier = Modifier.weight(0.2f), contentAlignment = Alignment.Center){
            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .background(
                        Color(hexToColor(category.color)),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(20.dp)
            )
        }
    }
//    Text(
//        text = category.name,
//        color = getContrastColor(category.color),
//        modifier = Modifier
//            .padding(10.dp)
//            .background(
//                Color(hexToColor(category.color)),
//                RoundedCornerShape(8.dp)
//            )
//            .fillMaxWidth()
//            .padding(20.dp),
//        fontSize = 20.sp,
//        fontFamily = FontFamily.SansSerif,
//        fontWeight = FontWeight.Medium,
//    )
}