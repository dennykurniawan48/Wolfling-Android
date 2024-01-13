package com.dennydev.wolfling.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dennydev.wolfling.model.common.ApiResponse
import com.dennydev.wolfling.navigation.Screen
import com.dennydev.wolfling.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetUsernameScreen(
    navController: NavController,
    homeViewModel: HomeViewModel
) {
    val username by homeViewModel.username.collectAsState(initial = "")
    val setusernameResponse by homeViewModel.changeUsernameResponse
    val token by homeViewModel.token.collectAsState(initial = "")
    val newUsername by homeViewModel.newUsername

    LaunchedEffect(key1 = username){
        if(username.isNotEmpty()){
            navController.navigate(Screen.HomeScreen.route){
                popUpTo(Screen.HomeScreen.route){
                    inclusive=true
                }
            }
        }
    }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(title={}, navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Default.ArrowBackIos, contentDescription = "Back")
            }
        })
    }) { it ->
        Column(modifier= Modifier
            .fillMaxSize()
            .padding(it)
            .padding(16.dp), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(48.dp))
            Row(modifier= Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Text("Username", style = MaterialTheme.typography.labelSmall)
                Text(setusernameResponse.message ?: "", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = newUsername, onValueChange = {
                homeViewModel.changeUsername(it)
            }, placeholder = { Text("Enter your username") }, leadingIcon = {
                Icon(imageVector = Icons.Default.Person, contentDescription = "Enter first name")
            }, modifier= Modifier.fillMaxWidth())
            Button(onClick = {
                if(setusernameResponse !is ApiResponse.Loading){
                    homeViewModel.setUsername(token)
                }
            }, modifier=Modifier.fillMaxWidth()) {
                Text("Set Username")
            }
        }
    }
}