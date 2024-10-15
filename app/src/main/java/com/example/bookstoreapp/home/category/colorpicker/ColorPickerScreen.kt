package com.example.bookstoreapp.home.category.colorpicker

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.bookstoreapp.AppUtils.hexToColor
import com.example.bookstoreapp.data.Category
import com.example.bookstoreapp.home.fragments.HomeButton
import com.example.bookstoreapp.retrofit.ApiViewModel
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

@Composable
fun ColorPickerScreen(
    api: ApiViewModel,
    index: Int,
    onColorPicked: () -> Unit
) {
    val categories by api.categories.observeAsState(emptyList())
    LaunchedEffect(Unit) {
        api.fetchCategories()
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        val controller = rememberColorPickerController()
        Column {
            HsvColorPicker(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp)
                    .padding(10.dp),
                controller = controller,
                initialColor = Color(hexToColor(categories[index].color)),
                onColorChanged = { colorEnvelope: ColorEnvelope ->
                    val color: Color = colorEnvelope.color // ARGB color value.
                    val hexCode: String = "#" + colorEnvelope.hexCode // Color hex code, which represents color value.
                    Log.d("HEX_CODE", hexCode)
                    categories[index].color = hexCode
                }
            )

            BrightnessSlider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .height(35.dp),
                controller = controller,
            )

            AlphaTile(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .height(35.dp)
                    .clip(RoundedCornerShape(6.dp)),
                controller = controller
            )

            Spacer(modifier = Modifier.height(50.dp))

            HomeButton(text = "Pick color") {
                onColorPicked()
            }
        }
    }
}