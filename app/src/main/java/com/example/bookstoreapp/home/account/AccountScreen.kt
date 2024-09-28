package com.example.bookstoreapp.home.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.bookstoreapp.R
import com.example.bookstoreapp.home.HomeButton
import com.example.bookstoreapp.login.AuthUtils
import com.example.bookstoreapp.login.EffectsViewModel
import com.example.bookstoreapp.login.google.UserData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun AccountScreen(
    userData: UserData?,
    onSignOut: () -> Unit
){
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(80.dp)
        ) {
            if (userData?.profilePictureUrl != null) {
                AsyncImage(
                    model = userData.profilePictureUrl,
                    contentDescription = "ProfilePicture",
                    modifier = Modifier
                        .padding(vertical = 40.dp)
                        .size(200.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.Black, CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.auth),
                    contentDescription = "LOGO",
                    modifier = Modifier
                        .padding(vertical = 40.dp)
                        .size(200.dp)
                )
            }

            HomeButton(text = "Sign out") {
                Firebase.auth.signOut()
                onSignOut()
            }
            Spacer(modifier = Modifier.height(150.dp))
        }
    }
}