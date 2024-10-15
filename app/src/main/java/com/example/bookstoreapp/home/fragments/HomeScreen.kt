package com.example.bookstoreapp.home.fragments

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bookstoreapp.data.Book
import com.example.bookstoreapp.data.Constants.STORAGE_CHILD
import com.example.bookstoreapp.data.ImageUtils
import com.example.bookstoreapp.graphs.HomeNavGraph
import com.example.bookstoreapp.home.NavItemState
import com.example.bookstoreapp.home.tasks.taskview.TaskViewAdd
import com.example.bookstoreapp.login.google.GoogleAuthUiClient
import com.example.bookstoreapp.retrofit.ApiViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.storage.ktx.storage


@Composable
fun HomeScreen(
    api: ApiViewModel,
    googleAuthUiClient: GoogleAuthUiClient,
    lifecycleScope: LifecycleCoroutineScope,
    rootNavController: NavHostController
) {
    val navController = rememberNavController()
    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }
    val floatingBottomState = rememberSaveable { (mutableStateOf(true)) }

    Scaffold(
        bottomBar = { BottomBar(navController = navController, bottomBarState) },
        floatingActionButton = { ActionButton(navController = navController, floatingBottomState) }
    ) {
        innerPadding -> HomeNavGraph(
            rootNavController = rootNavController,
            navController = navController,
            api,
            googleAuthUiClient,
            lifecycleScope,
            bottomBarState,
            floatingBottomState,
        )
    }
}

@Composable
fun BottomBar(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>
) {
    val screens = NavItemState.homeList

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    var bottomNavigationState by rememberSaveable {
        mutableIntStateOf(0)
    }

    val bottomBarDestination = screens.any { it.route == currentDestination?.route }
    Log.d("HomeScreenDebug", bottomBarDestination.toString())

    AnimatedVisibility(
        visible = bottomBarState.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        content = {
            NavigationBar(
                containerColor = Color.Black
            ) {
                NavItemState.homeList.forEachIndexed {
                    index, item -> NavigationBarItem(
                        selected = bottomNavigationState == index,
                        onClick = {
                            bottomNavigationState = index
                            navController.navigate(item.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        },
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (item.hasBadge) { Badge() }
                                    if (item.badgeNumber != 0) { Badge{ Text(text = item.title) } }
                                }
                            ) {
                                Icon(
                                    imageVector = if (bottomNavigationState == index) item.selectedIcon
                                    else item.unselectedIcon,
                                    contentDescription = item.title
                                )
                            }
                        },
                        label = { Text(text = item.title) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedTextColor = Color.White,
                            indicatorColor = Color.White
                        )
                    )
                }
            }
        }
    )
}


@Composable
fun ActionButton(
    navController: NavHostController,
    floatingBottomState: MutableState<Boolean>
) {
    AnimatedVisibility(
        visible = floatingBottomState.value,
        enter = slideInHorizontally(initialOffsetX = { it }),
        exit = slideOutHorizontally(targetOffsetX = { it }),
        content = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(TaskViewAdd)
                },
                shape = CircleShape,
                containerColor = Color.Black,
                contentColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(10.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    )
}