package com.dennydev.wolfling.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.dennydev.wolfling.model.common.ApiResponse
import com.dennydev.wolfling.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTweetScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel
) {
    val newTweet by homeViewModel.newTweet
    val token by homeViewModel.token.collectAsState(initial = "")
    val newTweetResponse by homeViewModel.newTweetResponse
    val isValidForm by homeViewModel.isValidNewTweetForm

    LaunchedEffect(key1 = newTweetResponse){
        if(newTweetResponse is ApiResponse.Success){
            homeViewModel.resetSendNewtTweet()
            navController.popBackStack()
        }
    }

    Scaffold(modifier = Modifier
        .fillMaxSize(), topBar = {
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .weight(0.1f)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = {
                    navController.popBackStack()
                }) {
                    Text("Cancel")
                }
                Button(onClick = {
                    homeViewModel.sendNewTweet(token, newTweet)
                }, enabled = isValidForm && newTweetResponse !is ApiResponse.Loading) {
                    Text("Tweet")
                }
            }
            Column(modifier = Modifier.weight(0.9f)) {
                TextField(value = newTweet, onValueChange = {
                    homeViewModel.changeTweet(it)
                }, placeholder = { Text(text = "What's your thinking?") },
                    modifier = Modifier.fillMaxSize(), maxLines = 7
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PrevNew() {
    Scaffold(modifier = Modifier
        .fillMaxSize(), topBar = {
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .weight(0.1f)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = {

                }) {
                    Text("Cancel")
                }
                Button(onClick = {

                }) {
                    Text("Tweet")
                }
            }
            Column(modifier = Modifier.weight(0.9f)) {
                TextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text(text = "What's your thinking?") },
                    modifier = Modifier.fillMaxSize(),
                    maxLines = 7
                )
            }
        }
    }
}