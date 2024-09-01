package com.example.bookstoreapp.home

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.bookstoreapp.data.Book
import com.example.bookstoreapp.data.Constants.FS_COLLECTION
import com.example.bookstoreapp.data.ImageUtils
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    onNotAuthenticated: () -> Unit
) {
    Log.d("HOME-SCREEN", "I AM HERE!")

    if (Firebase.auth.currentUser == null){
        onNotAuthenticated()
        Log.d("HOME-SCREEN", "NOT AUTHENTICATED!")
        return
    }
    Log.d("HOME-SCREEN", "AUTHENTICATED!")

    val fs = Firebase.firestore
    val storage = Firebase.storage.reference
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

    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        LazyColumn (
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
        ) {
            items(list.value){
                    book -> Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                ){
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        AsyncImage(
                            model = book.imageUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .height(100.dp)
                                .width(100.dp)
                                .padding(10.dp)
                        )
                        Text(
                            text = book.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth()
                                .padding(15.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
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
                .padding(10.dp),
            onClick = {
                Firebase.auth.signOut()
            }
        ) {
            Text(
                text = "Sign Out",
            )
        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}