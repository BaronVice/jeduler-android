package com.example.bookstoreapp.home

import android.content.res.Configuration
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.bookstoreapp.data.Book
import com.example.bookstoreapp.data.Constants.FS_COLLECTION
import com.example.bookstoreapp.data.ImageUtils
import com.google.firebase.auth.ktx.auth
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage


@Composable
fun HomeScreen(
    fs: FirebaseFirestore,
    storage: StorageReference,
) {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) {
        uri -> if (uri == null) return@rememberLauncherForActivityResult

        val uploadImgTask = storage.child( "test_img" ).putBytes(
            ImageUtils.bitmapToByteArray(context, uri)
        )
        uploadImgTask.addOnSuccessListener {
            uploadTask -> uploadTask.metadata?.reference?.downloadUrl?.addOnCompleteListener {
                uriTask ->  Book.saveBook(fs, uriTask.result.toString())
            }
        }
    }

    val list = remember {
        mutableStateOf(emptyList<Book>())
    }

    val listener = fs.collection( FS_COLLECTION.s ).addSnapshotListener {
            snapshot, exception -> list.value = snapshot?.toObjects(Book::class.java) ?: emptyList()
    }
    // listener.remove if snapShotListener is not needed

    var bottomNavigationState by rememberSaveable {
        mutableIntStateOf(0)
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.Black
            ) {
                NavItemState.homeList.forEachIndexed {
                    index, item -> NavigationBarItem(
                        selected = bottomNavigationState == index,
                        onClick = { bottomNavigationState = index },
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
                                    contentDescription = item.title)
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
    ){
        content -> Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LazyColumn (
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f)
            ) {
                items(list.value){
                    book -> Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                    ){
                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            AsyncImage(
                                model = book.imageUrl,
                                contentScale = ContentScale.Crop,
                                contentDescription = null,
                                modifier = Modifier
                                    .height(100.dp)
                                    .width(100.dp)
                                    .clip(CircleShape)
                                    .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
                            )
                            Surface (
                                shape = MaterialTheme.shapes.medium,
                                shadowElevation = 5.dp,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(10.dp, 0.dp, 0.dp, 0.dp)
                            ) {
                                Text(
                                    text = book.name,
                                    modifier = Modifier
                                        .padding(30.dp)
                                        .fillMaxSize()
                                        .wrapContentSize()
                                    ,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                onClick = {
                    launcher.launch(
                        PickVisualMediaRequest(
                            mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                }
            ) {
                Text(
                    text = "Add Book",
                )
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                onClick = {
                    Firebase.auth.signOut()
                }
            ) {
                Text(
                    text = "Sign Out",
                )
            }
            Spacer(modifier = Modifier.height(50.dp))
        }
    }


}