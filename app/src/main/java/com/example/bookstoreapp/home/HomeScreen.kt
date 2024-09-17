package com.example.bookstoreapp.home

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.bookstoreapp.AppUtils
import com.example.bookstoreapp.account.Account
import com.example.bookstoreapp.account.AccountScreen
import com.example.bookstoreapp.data.Book
import com.example.bookstoreapp.data.Constants.FS_COLLECTION
import com.example.bookstoreapp.data.Constants.STORAGE_CHILD
import com.example.bookstoreapp.data.ImageUtils
import com.example.bookstoreapp.graphs.HomeNavGraph
import com.example.bookstoreapp.search.Search
import com.example.bookstoreapp.search.SearchScreen
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.storage.ktx.storage


@Composable
fun HomeScreen(navController: NavHostController = rememberNavController()) {
    Scaffold(
        bottomBar = { BottomBar(navController = navController) },
        floatingActionButton = { ActionButton(navController = navController) }
    ) {
        innerPadding -> HomeNavGraph(navController = navController)
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = NavItemState.homeList

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    var bottomNavigationState by rememberSaveable {
        mutableIntStateOf(0)
    }

    val bottomBarDestination = screens.any { it.route == currentDestination?.route }
    Log.d("HomeScreenDebug", bottomBarDestination.toString())

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


@Composable
fun ActionButton(navController: NavHostController) {
    val context = LocalContext.current
    val fs = com.google.firebase.ktx.Firebase.firestore
    val storage = com.google.firebase.ktx.Firebase.storage.reference.child( STORAGE_CHILD.s )

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult

        val uploadImgTask = storage.child("test_img").putBytes(
            ImageUtils.bitmapToByteArray(context, uri)
        )
        uploadImgTask.addOnSuccessListener { uploadTask ->
            uploadTask.metadata?.reference?.downloadUrl?.addOnCompleteListener { uriTask ->
                Book.saveBook(fs, uriTask.result.toString())
            }
        }
    }
}
//
//    FloatingActionButton(
//        onClick = {
//            // TODO: open addTask view
////            launcher.launch(
////                PickVisualMediaRequest(
////                    mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
////                )
////            )
//        },
//        shape = CircleShape,
//        containerColor = Color.Black,
//        contentColor = Color.White,
//        elevation = FloatingActionButtonDefaults.elevation(10.dp)
//    ) {
//        Icon(Icons.Default.Add, contentDescription = "Add")
//    }
//}
//
//
//@Composable
//fun HomeScreeeen() {
//    val context = LocalContext.current
//    val fs = com.google.firebase.ktx.Firebase.firestore
//    val storage = com.google.firebase.ktx.Firebase.storage.reference.child( STORAGE_CHILD.s )
//
//    val launcher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.PickVisualMedia()
//    ) {
//        uri -> if (uri == null) return@rememberLauncherForActivityResult
//
//        val uploadImgTask = storage.child( "test_img" ).putBytes(
//            ImageUtils.bitmapToByteArray(context, uri)
//        )
//        uploadImgTask.addOnSuccessListener {
//            uploadTask -> uploadTask.metadata?.reference?.downloadUrl?.addOnCompleteListener {
//                uriTask ->  Book.saveBook(fs, uriTask.result.toString())
//            }
//        }
//    }
//
//    val list = remember {
//        mutableStateOf(emptyList<Book>())
//    }
//
//    val listener = fs.collection( FS_COLLECTION.s ).addSnapshotListener {
//            snapshot, exception -> list.value = snapshot?.toObjects(Book::class.java) ?: emptyList()
//    }
//    // listener.remove if snapShotListener is not needed
//
//    var bottomNavigationState by rememberSaveable {
//        mutableIntStateOf(0)
//    }
//
//    val navController = rememberNavController()
//
//    Scaffold(
//        bottomBar = {
//            NavigationBar(
//                containerColor = Color.Black
//            ) {
//                val navBackStackEntry by navController.currentBackStackEntryAsState()
//                val currentDestination = navBackStackEntry?.destination
//
//                NavItemState.homeList.forEachIndexed {
//                    index, item -> NavigationBarItem(
//                        selected = bottomNavigationState == index,
//                        onClick = {
//                            bottomNavigationState = index
//                            navController.navigate(item.route) {
//                                // Pop up to the start destination of the graph to
//                                // avoid building up a large stack of destinations
//                                // on the back stack as users select items
//                                popUpTo(navController.graph.findStartDestination().id) {
//                                    saveState = true
//                                }
//                                // Avoid multiple copies of the same destination when
//                                // reselecting the same item
//                                launchSingleTop = true
//                                // Restore state when reselecting a previously selected item
//                                restoreState = true
//                            }
//                        },
//                        icon = {
//                            BadgedBox(
//                                badge = {
//                                    if (item.hasBadge) { Badge() }
//                                    if (item.badgeNumber != 0) { Badge{ Text(text = item.title) } }
//                                }
//                            ) {
//                                Icon(
//                                    imageVector = if (bottomNavigationState == index) item.selectedIcon
//                                        else item.unselectedIcon,
//                                    contentDescription = item.title)
//                            }
//                        },
//                        label = { Text(text = item.title) },
//                        colors = NavigationBarItemDefaults.colors(
//                            selectedTextColor = Color.White,
//                            indicatorColor = Color.White
//                        )
//                    )
//                }
//            }
//        },
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = {
//                    launcher.launch(
//                        PickVisualMediaRequest(
//                            mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
//                        )
//                    )
//                },
//                shape = CircleShape,
//                containerColor = Color.Black,
//                contentColor = Color.White,
//                elevation = FloatingActionButtonDefaults.elevation(10.dp)
//            ) {
//                Icon(Icons.Default.Add, contentDescription = "Add")
//            }
//        }
//
//    ){
//        innerPadding ->
//        NavHost(navController = navController, startDestination = Account) {
//            composable<Home> {
//                HomeScreen()
//            }
//            composable<Search> {
//                SearchScreen()
//            }
//            composable<Account> {
//                AccountScreen {
//                    AppUtils.showToast(context, "Ay")
//                }
//            }
//        }
//
//        Column (
//            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.SpaceBetween
//        ) {
//            LazyColumn (
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .fillMaxHeight(0.85f)
//            ) {
//                items(list.value){
//                    book -> Card(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(10.dp)
//                    ){
//                        Row (
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(10.dp),
//                            verticalAlignment = Alignment.CenterVertically
//                        ){
//                            AsyncImage(
//                                model = book.imageUrl,
//                                contentScale = ContentScale.Crop,
//                                contentDescription = null,
//                                modifier = Modifier
//                                    .height(100.dp)
//                                    .width(100.dp)
//                                    .clip(CircleShape)
//                                    .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
//                            )
//                            Surface (
//                                shape = MaterialTheme.shapes.medium,
//                                shadowElevation = 5.dp,
//                                modifier = Modifier
//                                    .fillMaxSize()
//                                    .padding(10.dp, 0.dp, 0.dp, 0.dp)
//                            ) {
//                                Text(
//                                    text = book.name,
//                                    modifier = Modifier
//                                        .padding(30.dp)
//                                        .fillMaxSize()
//                                        .wrapContentSize()
//                                    ,
//                                    style = MaterialTheme.typography.bodyMedium
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//
//}