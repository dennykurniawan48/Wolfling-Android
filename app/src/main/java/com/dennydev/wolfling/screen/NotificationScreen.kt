package com.dennydev.wolfling.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.dennydev.wolfling.R
import com.dennydev.wolfling.component.BottomNav
import com.dennydev.wolfling.navigation.Screen
import com.dennydev.wolfling.viewmodel.HomeViewModel
import com.dennydev.wolfling.viewmodel.MainViewModel
import com.dennydev.wolfling.viewmodel.NotificationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    navController: NavHostController,
    notificationCount: Int,
    mainViewModel: MainViewModel,
    notificationViewModel: NotificationViewModel = hiltViewModel()
) {
    val notifications by notificationViewModel.notificationResponse
    val selectedBottomIndex by mainViewModel.selecteBottomNav
    val token by notificationViewModel.token.collectAsState(initial = "")

    LaunchedEffect(key1 = token){
        if(token.isNotEmpty()){
            notificationViewModel.getDetailNotification(token)
        }
    }

    Scaffold(modifier = Modifier
        .fillMaxSize(), topBar = {
        TopAppBar(title = { Text("All Notifications") })
    }, bottomBar = {
        BottomNav(
            navController = navController,
            selectedBottomIndex = selectedBottomIndex,
            notificationCount=notificationCount
        ){
            mainViewModel.onChangeSelectedBottomNav(it)
        }
    }){values ->

        notifications.data?.data?.let {
            LazyColumn(modifier= Modifier
                .fillMaxSize()
                .padding(values)
                .padding(vertical = 16.dp)) {
                items(items =it){
                    Card(modifier= Modifier
                        .fillMaxWidth()
                        .padding(6.dp)){
                        Row(modifier= Modifier
                            .fillMaxWidth()
                            .padding(8.dp), verticalAlignment = Alignment.CenterVertically){
                            Image(painter = painterResource(id = R.drawable.avatar), contentDescription = "", modifier=Modifier.size(50.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("${it.OriginUser.name} ${it.type} your post")
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}