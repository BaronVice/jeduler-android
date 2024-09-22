package com.example.bookstoreapp.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.bookstoreapp.home.account.Account
import com.example.bookstoreapp.home.search.Search
import com.example.bookstoreapp.home.tasks.Tasks

data class NavItemState<T: Any>(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasBadge: Boolean,
    val badgeNumber: Int,
    val route: T
){
    companion object{
        val homeList = listOf(
            NavItemState(
                title = "Tasks",
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home,
                hasBadge = false,
                badgeNumber = 0,
                Tasks
            ),
            NavItemState(
                title = "Search",
                selectedIcon = Icons.Filled.Search,
                unselectedIcon = Icons.Outlined.Search,
                hasBadge = false,
                badgeNumber = 0,
                Search
            ),
            NavItemState(
                title = "Account",
                selectedIcon = Icons.Filled.AccountCircle,
                unselectedIcon = Icons.Outlined.AccountCircle,
                hasBadge = false,
                badgeNumber = 0,
                Account
            ),
        )
    }
}
