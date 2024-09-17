package com.example.bookstoreapp.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.bookstoreapp.account.Account
import com.example.bookstoreapp.account.AccountScreen
import com.example.bookstoreapp.home.Home
import com.example.bookstoreapp.home.ScreenContent
import com.example.bookstoreapp.login.Auth
import com.example.bookstoreapp.login.Login
import com.example.bookstoreapp.search.Search

@Composable
fun HomeNavGraph(navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = Home
    ) {
        composable<Home>{
            ScreenContent(
                name = "Home",
                onClick = {}
            )
        }

        composable<Search>{
            ScreenContent(
                name = "Search",
                onClick = {}
            )
        }

        composable<Account>{
            AccountScreen {
                navController.navigate(Auth)
            }
        }
    }
}