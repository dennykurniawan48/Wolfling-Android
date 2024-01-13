package com.dennydev.wolfling

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.dennydev.wolfling.navigation.Screen
import com.dennydev.wolfling.navigation.SetupNavigation
import com.dennydev.wolfling.ui.theme.WolflingTheme
import com.dennydev.wolfling.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val mainViewModel: MainViewModel = hiltViewModel()
            val navController = rememberNavController()
            val isSignedIn by mainViewModel.isSignedIn

            WolflingTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val startDestination = if(isSignedIn) Screen.HomeScreen.route else Screen.LoginScreen.route
                    SetupNavigation(navController = navController, startDestination = startDestination, mainViewModel=mainViewModel)
                }
            }
        }
    }
}