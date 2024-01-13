package com.dennydev.wolfling.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val unselectedIcon: ImageVector, val selectedIcon: ImageVector, val route: String, val title: String) {
    object HomeScreen : Screen(Icons.Default.Home, Icons.Default.Home, "Home", "Home")
    object ProfileScreen : Screen(Icons.Default.Person, Icons.Default.Person, "Profile", "Profile")
    object UserProfileScreen : Screen(Icons.Default.Person, Icons.Default.Person, "User/{id}", "User")
    object LoginScreen : Screen(Icons.Default.Check, Icons.Default.Check, "Login", "Login")
    object RegisterScreen : Screen(Icons.Default.Check, Icons.Default.Check, "Register", "Register")
    object SetUsernameScreen : Screen(Icons.Default.Check, Icons.Default.Check, "Setusername", "Setusername")
    object NewTweetScreen: Screen(Icons.Default.Check, Icons.Default.Check, "NewTweet", "NewTweet")
    object DetailTweetScreen: Screen(Icons.Default.Check, Icons.Default.Check, "Detail/{id}", "Detail")
    object NotificationScreen: Screen(Icons.Default.NotificationsNone, Icons.Default.NotificationsNone, "Notification", "Notification")
}