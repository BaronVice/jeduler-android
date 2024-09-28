package com.example.bookstoreapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.bookstoreapp.graphs.RootNavigationGraph
import com.example.bookstoreapp.login.google.GoogleAuthUiClient
import com.google.android.gms.auth.api.identity.Identity

class MainActivity : ComponentActivity() {

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //        enableEdgeToEdge()
        setContent {
            RootNavigationGraph(
                googleAuthUiClient,
                navController = rememberNavController(),
                lifecycleScope = lifecycleScope
            )
//
//            val insetsController = WindowCompat.getInsetsController(window, window.decorView)
//            insetsController.apply {
//                hide(WindowInsetsCompat.Type.systemBars())
//                systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//            }
        }
    }
}

