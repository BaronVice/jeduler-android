package com.example.bookstoreapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.bookstoreapp.account.Account
import com.example.bookstoreapp.account.AccountScreen
import com.example.bookstoreapp.home.Home
import com.example.bookstoreapp.home.HomeScreen
import com.example.bookstoreapp.login.Login
import com.example.bookstoreapp.login.LoginScreen

@Composable
fun Nav(
    navController: NavHostController
){
    NavHost(
        navController = navController,
        startDestination = Login
    ){
        composable<Home>{
            HomeScreen()
        }
        composable<Login>{
            LoginScreen {
                // TODO: animation here, to wait until auth is loaded
                navController.popBackStack()
                navController.navigate( Home )
            }
        }
    }
}