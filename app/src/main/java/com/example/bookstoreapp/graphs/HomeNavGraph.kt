package com.example.bookstoreapp.graphs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.bookstoreapp.data.Category
import com.example.bookstoreapp.home.account.Account
import com.example.bookstoreapp.home.account.AccountScreen
import com.example.bookstoreapp.home.ScreenContent
import com.example.bookstoreapp.home.category.Categories
import com.example.bookstoreapp.home.category.CategoriesScreen
import com.example.bookstoreapp.home.category.colorpicker.ColorPicker
import com.example.bookstoreapp.home.tasks.Tasks
import com.example.bookstoreapp.home.tasks.TasksScreen
import com.example.bookstoreapp.login.Auth
import com.example.bookstoreapp.home.search.Search

@Composable
fun HomeNavGraph(
    rootNavController: NavHostController,
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>,
    floatingBottomState: MutableState<Boolean>,
    categories: SnapshotStateList<Category>
){
    NavHost(
        navController = navController,
        startDestination = Tasks
    ) {
        composable<Tasks>{
            TasksScreen(categories){
                navController.navigate(Categories)
            }
            bottomBarState.value = true
            floatingBottomState.value = true
        }

        composable<Search>{
            ScreenContent(
                name = "Search",
                onClick = {}
            )
            bottomBarState.value = true
            floatingBottomState.value = false
        }

        composable<Account>{
            AccountScreen {
                navController.popBackStack()
                rootNavController.popBackStack()
                rootNavController.navigate(Auth)
                bottomBarState.value = false
                floatingBottomState.value = false
            }
            bottomBarState.value = true
            floatingBottomState.value = false
        }

        composable<Categories>{
            CategoriesScreen(categories, navController /* TODO: navController */){
//                navController.popBackStack()
                navController.navigate(Tasks)
            }
            bottomBarState.value = false
            floatingBottomState.value = false
        }

        composable<Category> {
            backStackEntry ->
            val category: Category = backStackEntry.toRoute()
            ScreenContent(name = category.name) {

            }
        }
    }
}