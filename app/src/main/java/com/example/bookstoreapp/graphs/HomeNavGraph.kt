package com.example.bookstoreapp.graphs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.bookstoreapp.data.Category
import com.example.bookstoreapp.data.Task
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
import com.example.bookstoreapp.home.tasks.taskview.TaskCategoryLists
import com.example.bookstoreapp.home.tasks.taskview.TaskView
import com.example.bookstoreapp.home.tasks.taskview.TaskViewScreen
import com.example.bookstoreapp.login.google.GoogleAuthUiClient
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun HomeNavGraph(
    rootNavController: NavHostController,
    navController: NavHostController,
    googleAuthUiClient: GoogleAuthUiClient,
    lifecycleScope: LifecycleCoroutineScope,
    bottomBarState: MutableState<Boolean>,
    floatingBottomState: MutableState<Boolean>,
){
    val categories = remember {
        mutableStateListOf(
            Category(1, "Main", "#960018"),
            Category(2, "Main", "#405919"),
            Category(3, "Main", "#468499"),
            Category(4, "Main", "#c77765"),
            Category(5, "Main", "#ff80b0"),
            Category(6, "Main", "#6a1c89"),
            Category(7, "Main", "#f07819"),
            Category(8, "Main", "#c77765"),
            Category(9, "Main", "#ff80b0"),
            Category(10, "Main", "#ffffff")
        )
    }
    val tasks = remember {
        mutableStateListOf(
            Task(
                1,
                "Weird1",
                "Aboba",
                Random.nextBoolean(),
                Random.nextInt(1,4).toShort(),
                mutableListOf(),
                listOf(),
                "22.10.2024+09:07",
                "22.10.2024+09:07"
            ),
            Task(
                2,
                "Weird2",
                "Aboba",
                Random.nextBoolean(),
                Random.nextInt(1,4).toShort(),
                mutableListOf(),
                listOf(),
                "22.10.2024+09:07",
                "22.10.2024+09:07"
            ),
            Task(
                3,
                "Weird3",
                "Aboba",
                Random.nextBoolean(),
                Random.nextInt(1,4).toShort(),
                mutableListOf(),
                listOf(),
                "22.10.2024+09:07",
                "22.10.2024+09:07"
            ),
            Task(
                4,
                "Weird4",
                "Aboba",
                Random.nextBoolean(),
                Random.nextInt(1,4).toShort(),
                mutableListOf(),
                listOf(),
                "22.10.2024+09:07",
                "22.10.2024+09:07"
            ),
            Task(
                5,
                "Weird5",
                "Aboba",
                Random.nextBoolean(),
                Random.nextInt(1,4).toShort(),
                mutableListOf(),
                listOf(),
                "22.10.2024+09:07",
                "22.10.2024+09:07"
            ),
            Task(
                6,
                "Weird6",
                "Aboba",
                Random.nextBoolean(),
                Random.nextInt(1,4).toShort(),
                mutableListOf(),
                listOf(),
                "22.10.2024+09:07",
                "22.10.2024+09:07"
            ),
            Task(
                7,
                "Weird7",
                "Aboba",
                Random.nextBoolean(),
                Random.nextInt(1,4).toShort(),
                mutableListOf(),
                listOf(),
                "22.10.2024+09:07",
                "22.10.2024+09:07"
            ),
            Task(
                8,
                "Weird8",
                "Aboba",
                Random.nextBoolean(),
                Random.nextInt(1,4).toShort(),
                mutableListOf(),
                listOf(),
                "22.10.2024+09:07",
                "22.10.2024+09:07"
            ),
            Task(
                9,
                "Weird9",
                "Aboba",
                Random.nextBoolean(),
                Random.nextInt(1,4).toShort(),
                mutableListOf(),
                listOf(),
                "22.10.2024+09:07",
                "22.10.2024+09:07"
            ),
            Task(
                10,
                "Weird10",
                "Aboba",
                Random.nextBoolean(),
                Random.nextInt(1,4).toShort(),
                mutableListOf(),
                listOf(),
                "22.10.2024+09:07",
                "22.10.2024+09:07"
            )
        )
    }

    NavHost(
        navController = navController,
        startDestination = Tasks
    ) {
        composable<Tasks>{
            TasksScreen(
                navController,
                categories,
                tasks
            ){
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
            CategoriesScreen(categories, navController){
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

        composable<TaskView> {
            backStackEntry ->
            val taskView: TaskView = backStackEntry.toRoute()

            TaskViewScreen(
                TaskCategoryLists(
                    categories.toList()
                ),
                tasks[taskView.id]
//                taskView.id
            ) {
                navController.popBackStack()
                navController.navigate(Tasks)
            }
            bottomBarState.value = false
            floatingBottomState.value = false
        }
    }
}