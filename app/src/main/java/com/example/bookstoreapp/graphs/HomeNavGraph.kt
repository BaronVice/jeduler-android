package com.example.bookstoreapp.graphs

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.bookstoreapp.home.account.Account
import com.example.bookstoreapp.home.account.AccountScreen
import com.example.bookstoreapp.home.category.Categories
import com.example.bookstoreapp.home.category.CategoriesScreen
import com.example.bookstoreapp.home.category.colorpicker.ColorPicker
import com.example.bookstoreapp.home.category.colorpicker.ColorPickerScreen
import com.example.bookstoreapp.home.tasks.Tasks
import com.example.bookstoreapp.home.tasks.TasksScreen
import com.example.bookstoreapp.login.navroots.Auth
import com.example.bookstoreapp.home.search.Search
import com.example.bookstoreapp.home.search.SearchScreen
import com.example.bookstoreapp.home.search.result.SearchResult
import com.example.bookstoreapp.home.search.result.SearchResultScreen
import com.example.bookstoreapp.home.tasks.taskview.CategoryList
import com.example.bookstoreapp.home.tasks.taskview.TaskView
import com.example.bookstoreapp.home.tasks.taskview.TaskViewAdd
import com.example.bookstoreapp.home.tasks.taskview.TaskViewScreen
import com.example.bookstoreapp.login.google.GoogleAuthUiClient
import com.example.bookstoreapp.retrofit.ApiViewModel
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun HomeNavGraph(
    rootNavController: NavHostController,
    navController: NavHostController,
    api: ApiViewModel,
    googleAuthUiClient: GoogleAuthUiClient,
    lifecycleScope: LifecycleCoroutineScope,
    bottomBarState: MutableState<Boolean>,
    floatingBottomState: MutableState<Boolean>,
){
//    val categories = remember {
//        mutableStateListOf(
//            Category(1, "Sweet", "#960018"),
//            Category(2, "Dreams", "#405919"),
//            Category(3, "Are", "#468499"),
//            Category(4, "Made of", "#c77765"),
//            Category(5, "This", "#ff80b0"),
//        )
//    }

    NavHost(
        navController = navController,
        startDestination = Tasks
    ) {
        composable<Tasks>{
            TasksScreen(
                api,
                onTaskClick = {
                    id -> navController.navigate(
                        TaskView(id, "")
                    )
                },
                onCategoriesClick = {
                    navController.popBackStack()
                    navController.navigate(Categories)
                }
            )

            bottomBarState.value = true
            floatingBottomState.value = true
        }

        composable<Search>{
            SearchScreen(
                api
            ){
                s -> navController.navigate(SearchResult(s))
            }
            bottomBarState.value = true
            floatingBottomState.value = false
        }

        composable<SearchResult> {
            backStackEntry ->
            val searchResult: SearchResult = backStackEntry.toRoute()

            SearchResultScreen(
                api,
                onTaskClick = {
                    id -> navController.navigate(
                        TaskView(id, searchResult.url)
                    )
                },
                onNavBack = {
                    navController.popBackStack()
                    navController.navigate(Search)
                }
            )

            bottomBarState.value = false
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
            CategoriesScreen(
                api,
                navController
            ){
                navController.popBackStack()
                navController.navigate(Tasks)
            }
            bottomBarState.value = false
            floatingBottomState.value = false
        }

        composable<ColorPicker> {
            backStackEntry ->
            val taskView: ColorPicker = backStackEntry.toRoute()
            val id = taskView.id.toShort()
            ColorPickerScreen(
                api,
                id
            ) {
                category ->
                val originalCategory = api.categories.value!!.find { c -> c.id == id }!!
                if (category != originalCategory){
                    originalCategory.name = category.name
                    originalCategory.color = category.color
                }

                api.updateCategory(originalCategory)

                navController.popBackStack()
                navController.navigate(Categories)
            }
        }

        composable<TaskView> {
            backStackEntry ->
            val taskView: TaskView = backStackEntry.toRoute()

            TaskViewScreen(
                api,
                taskId = taskView.id,
                firstButtonText = "Save",
                firstButtonAction = {
                    task ->
                    val originalTask = api.tasks.value!!.find { t -> t.id!! == taskView.id }!!
                    if (task != originalTask){
                        originalTask.name = task.name
                        originalTask.description = task.description
                        originalTask.priority = task.priority
                        originalTask.categoryIds = task.categoryIds
                        originalTask.taskDone = task.taskDone
                        originalTask.subtasks = task.subtasks
                        originalTask.startsAt = task.startsAt
                        originalTask.notifyAt = task.notifyAt
                    }

                    api.updateTask(originalTask)
                }
            ) {
                navController.popBackStack()
                if (taskView.url == ""){
                    navController.navigate(Tasks)
                } else {
                    navController.navigate(SearchResult(taskView.url))
                }
            }
            bottomBarState.value = false
            floatingBottomState.value = false
        }

        composable<TaskViewAdd> {
            TaskViewScreen(
                api,
                -1,
                firstButtonText = "Add and to tasks",
                firstButtonAction = {
                    task -> // request on add
                    api.createTask(task)
                    navController.popBackStack()
                    navController.navigate(Tasks)
                }
            ) {
                navController.popBackStack()
                navController.navigate(Tasks)
            }
            bottomBarState.value = false
            floatingBottomState.value = false
        }
    }
}