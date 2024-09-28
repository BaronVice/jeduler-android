package com.example.bookstoreapp.home.category

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.bookstoreapp.AppUtils.getContrastColor
import com.example.bookstoreapp.AppUtils.getRandomCategoryName
import com.example.bookstoreapp.AppUtils.getRandomHex
import com.example.bookstoreapp.AppUtils.hexToColor
import com.example.bookstoreapp.AppUtils.showToast
import com.example.bookstoreapp.data.Category
import com.example.bookstoreapp.home.HomeButton
import com.example.bookstoreapp.home.category.colorpicker.ColorPicker
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun CategoriesScreen(
    categories: SnapshotStateList<Category>,
    navController: NavHostController,
    onPopBack: () -> Unit
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 5.dp, vertical = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        val lazyColumnListState = rememberLazyListState()
        val corroutineScope = rememberCoroutineScope()

        val context = LocalContext.current
        Column{
            Box(
                modifier = Modifier.weight(0.75f)
            ){
                LazyColumn(
                    state = lazyColumnListState
                ) {
                    items(categories, key = {c -> c.id}){
                        category -> CategoryHolderEdit(navController, categories, category)
                    }
                }
            }
            Box(
                modifier = Modifier.weight(0.2f)
            ){
                Column {
                    HomeButton(text = "Add category") {
                        if (categories.size < 16){
                            categories.add(
                                // request on add to get id
                                Category(
                                    Random.nextInt(100, 10000).toShort(), // TODO: replace with id from request
                                    getRandomCategoryName(categories),
                                    getRandomHex()
                                )
                            )
                            corroutineScope.launch {
                                lazyColumnListState.scrollToItem(categories.size-1)
                            }
                        } else {
                            showToast(
                                context,
                                "You`ve reached categories limit (16).\nDelete old to add new.")
                        }
                    }
                    HomeButton(text = "To tasks") {
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
    navController: NavHostController,
    categories: SnapshotStateList<Category>,
    category: Category
){
    Row{
        val mainColor = Color(hexToColor(category.color))
        val contrastColor = getContrastColor(category.color)
        val focusManager = LocalFocusManager.current
        val context = LocalContext.current

        Box(modifier = Modifier.weight(0.8f), contentAlignment = Alignment.Center){
            val name = remember { mutableStateOf(category.name) }
            val cursorColor = remember { mutableStateOf(contrastColor) }

            TextField(
                value = name.value,
                onValueChange = {
                    if (it.length <= 20) {
                        category.name = it
                        name.value = it
                    } else {
                        showToast(context, "Max length is 20 chars")
                    }
                    if (category.name == ""){
                        cursorColor.value = Color.Transparent
                    } else {
                        cursorColor.value = contrastColor
                    }
                },
                textStyle = LocalTextStyle.current.copy(
                    color = contrastColor,
                    fontSize = 20.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                placeholder = {
                    Text(
                        text = "Oh no! Will I be deleted?",
                        color = contrastColor,
                        fontSize = 20.sp,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Light,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = mainColor,
                    focusedContainerColor = mainColor,
                    cursorColor = cursorColor.value,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
                    .onFocusChanged { focusState ->
                        if (!focusState.isFocused) {
                            if (name.value == "") {
                                categories.removeIf { c -> c.id == category.id }
                            }
                        }
                    },
                singleLine = true
            )
        }
        Box(modifier = Modifier.weight(0.25f), contentAlignment = Alignment.Center){
            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
                    .background(
                        Color(hexToColor(category.color)),
                        RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        if (category.name != ""){
                            focusManager.clearFocus()

                            navController.navigate(
                                ColorPicker(categories.indexOfFirst { c -> c.id == category.id })
                            )
                        }
                    }
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