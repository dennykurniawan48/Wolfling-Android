package com.dennydev.wolfling.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.dennydev.wolfling.screen.DetailTweetScreen
import com.dennydev.wolfling.screen.HomeScreen
import com.dennydev.wolfling.screen.LoginScreen
import com.dennydev.wolfling.screen.NewTweetScreen
import com.dennydev.wolfling.screen.NotificationScreen
import com.dennydev.wolfling.screen.ProfileScreen
import com.dennydev.wolfling.screen.RegisterScreen
import com.dennydev.wolfling.screen.SetUsernameScreen
import com.dennydev.wolfling.viewmodel.HomeViewModel
import com.dennydev.wolfling.viewmodel.MainViewModel

@Composable
fun SetupNavigation(navController: NavHostController, startDestination: String, mainViewModel: MainViewModel) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val notificationCount by homeViewModel.newNotification

    NavHost(navController = navController, startDestination = startDestination, modifier = Modifier) {
        composable(Screen.HomeScreen.route){ entry ->
            HomeScreen(navController = navController, mainViewModel=mainViewModel, homeViewModel = homeViewModel, notificationCount = notificationCount)
        }
        composable(Screen.LoginScreen.route){
            LoginScreen(navController = navController)
        }
        composable(Screen.RegisterScreen.route){
            RegisterScreen(navController = navController)
        }
        composable(Screen.SetUsernameScreen.route){
            SetUsernameScreen(navController = navController, homeViewModel=homeViewModel)
        }
        composable(Screen.ProfileScreen.route){
            ProfileScreen(navController = navController, mainViewModel, homeViewModel=homeViewModel, notificationCount = notificationCount)
        }
        composable(Screen.NewTweetScreen.route){
            NewTweetScreen(navController = navController, homeViewModel)
        }
        composable(Screen.NotificationScreen.route){
            NotificationScreen(navController = navController, notificationCount, mainViewModel)
        }
        composable(Screen.DetailTweetScreen.route, arguments = listOf(
            navArgument(name="id"){
                type = NavType.StringType
            })){
            it.arguments?.getString("id")?.let {
                DetailTweetScreen(navController = navController, idTweet = it)
            }
        }
        composable(Screen.UserProfileScreen.route, arguments = listOf(
            navArgument(name="id"){
                type = NavType.StringType
            })){
            it.arguments?.getString("id")?.let {
                ProfileScreen(navController = navController, mainViewModel = mainViewModel, notificationCount = notificationCount, profileUsername = it, homeViewModel = homeViewModel)
            }
        }
    }
}