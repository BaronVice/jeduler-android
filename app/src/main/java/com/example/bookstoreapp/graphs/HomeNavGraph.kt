package com.example.bookstoreapp.graphs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LifecycleCoroutineScope
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
import com.example.bookstoreapp.home.category.colorpicker.ColorPickerScreen
import com.example.bookstoreapp.home.tasks.Tasks
import com.example.bookstoreapp.home.tasks.TasksScreen
import com.example.bookstoreapp.login.navroots.Auth
import com.example.bookstoreapp.home.search.Search
import com.example.bookstoreapp.login.google.GoogleAuthUiClient
import kotlinx.coroutines.launch

@Composable
fun HomeNavGraph(
    rootNavController: NavHostController,
    navController: NavHostController,
    googleAuthUiClient: GoogleAuthUiClient,
    lifecycleScope: LifecycleCoroutineScope,
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
            AccountScreen(
                googleAuthUiClient.getSignedInUser()
            ) {
                lifecycleScope.launch {
                    googleAuthUiClient.signOut()

                    navController.popBackStack()
                    rootNavController.popBackStack()
                    rootNavController.navigate(Auth)
                    bottomBarState.value = false
                    floatingBottomState.value = false
                }

            }
            bottomBarState.value = true
            floatingBottomState.value = false
        }

        composable<Categories>{
            CategoriesScreen(categories, navController /* TODO: navController */){
                navController.popBackStack()
                navController.navigate(Tasks)
            }
            bottomBarState.value = false
            floatingBottomState.value = false
        }

        composable<ColorPicker> {
            backStackEntry ->
            val colorPicker: ColorPicker = backStackEntry.toRoute()
            ColorPickerScreen(
                categories,
                colorPicker.id
            ) {
                navController.popBackStack()
                navController.navigate(Categories)
            }
        }
    }
}