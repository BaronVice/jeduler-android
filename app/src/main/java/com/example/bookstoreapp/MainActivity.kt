package com.example.bookstoreapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.bookstoreapp.data.Book
import com.example.bookstoreapp.data.Constants.*
import com.example.bookstoreapp.data.ImageUtils
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class MainActivity : ComponentActivity() {
    private lateinit var fs: FirebaseFirestore
    private lateinit var storage: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fs = Firebase.firestore
        storage = Firebase.storage.reference.child( STORAGE_CHILD.s )

        setContent {
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.PickVisualMedia()
            ) {
                uri -> if (uri == null) return@rememberLauncherForActivityResult

                val uploadImgTask = storage.child( "test_img" ).putBytes(
                    ImageUtils.bitmapToByteArray(this, uri)
                )
                uploadImgTask.addOnSuccessListener {
                    uploadTask -> uploadTask.metadata?.reference?.downloadUrl?.addOnCompleteListener {
                        uriTask ->  Book.saveBook(fs, uriTask.result.toString())
                    }
                }
            }

            MainScreen(fs, storage){
                launcher.launch(
                    PickVisualMediaRequest(
                        mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }

        }
    }
}

@Composable
fun MainScreen(
    fs: FirebaseFirestore,
    storage: StorageReference,
    onClick: () -> Unit
) {
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
                                .padding(15.dp))
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
                onClick()
            }
        ) {
            Text(
                text = "Add Book",
            )
        }
    }
}