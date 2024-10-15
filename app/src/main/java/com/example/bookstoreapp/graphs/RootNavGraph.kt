package com.example.bookstoreapp.graphs

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.bookstoreapp.home.Home
import com.example.bookstoreapp.home.fragments.HomeScreen
import com.example.bookstoreapp.login.navroots.Auth
import com.example.bookstoreapp.login.navroots.Login
import com.example.bookstoreapp.login.LoginScreen
import com.example.bookstoreapp.login.google.GoogleAuthUiClient
import com.example.bookstoreapp.login.google.SignInViewModel
import com.example.bookstoreapp.retrofit.ApiViewModel
import kotlinx.coroutines.launch

@Composable
fun RootNavigationGraph(
    api: ApiViewModel,
    googleAuthUiClient: GoogleAuthUiClient,
    navController: NavHostController,
    lifecycleScope: LifecycleCoroutineScope
){
    NavHost(
        navController = navController,
        startDestination = Auth
    ){
        navigation<Auth>(
            startDestination = Login
        ){
            composable<Login> {
                val signInViewModel = viewModel<SignInViewModel>()
                val state by signInViewModel.state.collectAsStateWithLifecycle()
                val signInButtonState = remember { mutableStateOf(true) }

                // Check if already signed-in
                LaunchedEffect(Unit) {
                    if (googleAuthUiClient.getSignedInUser() != null) {
                        navController.navigate(Home)
                    }
                }

                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                    onResult = {
                        result ->
                        if (result.resultCode == RESULT_OK){
                            lifecycleScope.launch {
                                val signInResult = googleAuthUiClient.signInWithIntent(
                                    intent = result.data?: return@launch
                                )
                                signInViewModel.onSignInResult(signInResult)
                            }
                        }
                        if (result.resultCode == RESULT_CANCELED){
                            signInButtonState.value = true
                        }
                    }
                )

                LaunchedEffect(key1 = state.isSignInSuccessful) {
                    if (state.isSignInSuccessful){
                        navController.popBackStack()
                        navController.navigate( Home )
                        // New state after sign o
                        signInViewModel.resetState()
                    }
                }

                LoginScreen(
                    state,
                    signInButtonState
                ) {
                    signInButtonState.value = false
                    lifecycleScope.launch {
                        val signInIntentSender = googleAuthUiClient.signIn()
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                signInIntentSender ?: return@launch
                            ).build()
                        )
                    }
                }
            }
        }

        composable<Home>{
            HomeScreen(
                api,
                googleAuthUiClient,
                lifecycleScope,
                navController
            )
        }
    }
}