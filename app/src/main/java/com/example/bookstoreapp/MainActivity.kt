package com.example.bookstoreapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import coil.compose.AsyncImage
import com.example.bookstoreapp.data.Book
import com.example.bookstoreapp.data.Constants.*
import com.example.bookstoreapp.home.Home
import com.example.bookstoreapp.home.HomeScreen
import com.example.bookstoreapp.login.EffectsViewModel
import com.example.bookstoreapp.login.Login
import com.example.bookstoreapp.login.LoginScreen
import com.example.bookstoreapp.ui.theme.LogoFilter1
import com.example.bookstoreapp.ui.theme.LogoFilter2
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.delay


class MainActivity : ComponentActivity() {
    private lateinit var fs: FirebaseFirestore
    private lateinit var storage: StorageReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fs = Firebase.firestore
        storage = Firebase.storage.reference.child( STORAGE_CHILD.s )

        val effectsViewModelBg = EffectsViewModel()
        val effectsViewModelLogo = EffectsViewModel()
        effectsViewModelLogo.color2 = LogoFilter1
        effectsViewModelLogo.color1 = LogoFilter2

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = Login
            ){
                composable<Home>{
                    // TODO: BRUUUUUUUUUUUUUUUUUUUUUUUUUH....................
                    //  learn coroutines dammit...
                    var a = 1
                    for(i in 1..100000000){
                        a += a + a
                    }
                    HomeScreen(
                        fs, storage
                    )
                }
                composable<Login>{
                    LoginScreen(
                        fs, storage, effectsViewModelBg, effectsViewModelLogo
                    ) {
                        // TODO: animation here, to wait until auth is loaded
                        navController.navigate( Home )
                    }
                }
            }
        }
    }
}

