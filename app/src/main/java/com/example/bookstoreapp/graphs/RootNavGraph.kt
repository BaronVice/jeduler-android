package com.example.bookstoreapp.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.bookstoreapp.home.Home
import com.example.bookstoreapp.home.HomeScreen
import com.example.bookstoreapp.login.Auth
import com.example.bookstoreapp.login.Login
import com.example.bookstoreapp.login.LoginScreen

@Composable
fun RootNavigationGraph(
    navController: NavHostController
){
    NavHost(
        navController = navController,
        startDestination = Auth
    ){
        navigation<Auth>(
            startDestination = Login
        ){
            composable<Login> {
                LoginScreen {
                    navController.popBackStack()
                    navController.navigate( Home )
                }
            }
        }

        composable<Home>{
            HomeScreen(navController)
        }
    }
}