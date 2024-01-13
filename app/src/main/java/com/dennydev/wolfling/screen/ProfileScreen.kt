package com.dennydev.wolfling.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.dennydev.wolfling.R
import com.dennydev.wolfling.component.BottomNav
import com.dennydev.wolfling.component.common.LoadingTweet
import com.dennydev.wolfling.component.common.Tweet
import com.dennydev.wolfling.component.home.EmptyResult
import com.dennydev.wolfling.component.login.googleSignOut
import com.dennydev.wolfling.model.listtweet.Tweet
import com.dennydev.wolfling.model.notification.NotificationCount
import com.dennydev.wolfling.navigation.Screen
import com.dennydev.wolfling.viewmodel.HomeViewModel
import com.dennydev.wolfling.viewmodel.MainViewModel
import com.dennydev.wolfling.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    profileUsername: String?=null,
    homeViewModel: HomeViewModel,
    viewModel: ProfileViewModel = hiltViewModel(key = if(profileUsername==null) "ownprofile" else "userprofile$profileUsername"),
    notificationCount: Int
) {
    val token by viewModel.token.collectAsState(initial = "")
    val username by viewModel.username.collectAsState(initial = "")
    val isLoginWithGoogle by viewModel.isGoogle.collectAsState(initial = false)
    val profile by viewModel.profileData
    val tweets = viewModel.tweets.collectAsLazyPagingItems()
    val selectedBottomIndex by mainViewModel.selecteBottomNav
    val followed by viewModel.following
    val context = LocalContext.current

    fun addLike(tweet: Tweet){
        viewModel.likeAction(tweet)
    }

    fun addRetweet(tweet: Tweet){
        viewModel.retweetAction(tweet)
    }

    fun addReply(tweet: Tweet){
        navController.navigate(Screen.DetailTweetScreen.route.replace("{id}", tweet.id))
    }

    LaunchedEffect(key1 = token, username){
        if(profileUsername==null) {
            if (token.isNotEmpty() && username.isNotEmpty()) {
                viewModel.getProfile(token, username)
                viewModel.loadItems(token, username)
            }
        }else{
            if (token.isNotEmpty()) {
                viewModel.getProfile(token, profileUsername)
                viewModel.loadItems(token, profileUsername)
            }
        }
    }
    Scaffold(modifier = Modifier
        .fillMaxSize(), topBar = {

    }, bottomBar = {
        if(profileUsername == null) {
            BottomNav(
                navController,
                selectedBottomIndex,
                notificationCount
            ) {
                mainViewModel.onChangeSelectedBottomNav(it)
            }
        }
    }) { values ->
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(values)) {
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.header),
                            contentScale = ContentScale.FillWidth,
                            contentDescription = "",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                        )
                        Row(modifier = Modifier.padding(top = 165.dp, start = 16.dp)) {
                            Image(
                                painter = painterResource(id = R.drawable.avatar),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(100.dp)
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(0.65f)) {
                            Text(
                                profile.data?.data?.name ?: "",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "@${profile.data?.data?.username ?: ""}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Light
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        if(profileUsername != null) {
                            Button(onClick = {
                                profile.data?.data?.let{
                                    viewModel.follow(token,it.id)
                                }
                            }, modifier = Modifier.weight(0.35f)) {
                                Text(if(followed) "Unfollow" else "Follow")
                            }
                        }else{
                            Button(onClick = {
                                if(isLoginWithGoogle) {
                                    googleSignOut(context) {
                                        homeViewModel.logout()
                                    }
                                }else{
                                    homeViewModel.logout()
                                }

                                navController.navigate(Screen.LoginScreen.route){
                                    popUpTo(Screen.LoginScreen.route){
                                        inclusive=true
                                    }
                                }
                            }, modifier = Modifier.weight(0.35f)) {
                                Text("Logout")
                            }
                        }
                    }
                }
            }

            if (tweets.loadState.refresh is LoadState.NotLoading && tweets.itemCount > 0) {
                items(tweets.itemCount) { item ->
                    tweets.get(item)?.let {
                        Tweet(tweet = it, addLike = {
                            addLike(it)
                        }, addRetweet = {
                            addRetweet(it)
                        }, addReply={
                            addReply(it)
                        }, onProfileClick = {
                            if(it.user.username == username){
                                navController.navigate(Screen.ProfileScreen.route)
                            }else{
                                navController.navigate(Screen.UserProfileScreen.route.replace("{id}", it.user.username.toString()))
                            }
                        })
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                item {
                    if (tweets.loadState.append is LoadState.Loading && !tweets.loadState.append.endOfPaginationReached) {
                        LoadingTweet()
                    }
                }
            } else if (tweets.loadState.refresh is LoadState.NotLoading && tweets.loadState.append is LoadState.NotLoading && !tweets.loadState.refresh.endOfPaginationReached && tweets.itemCount == 0) {
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    EmptyResult()
                }
            } else if (tweets.loadState.refresh is LoadState.NotLoading && tweets.loadState.append is LoadState.Loading && !tweets.loadState.refresh.endOfPaginationReached && tweets.itemCount == 0) {
                item {
                    Spacer(modifier = Modifier.height(48.dp))
                    EmptyResult()
                }
            } else if (tweets.loadState.refresh is LoadState.Loading) {
                item {
                    LoadingTweet()
                }
            }
        }
    }
}

@Preview
@Composable
fun PrevProfile() {
    LazyColumn(modifier=Modifier.fillMaxSize()){
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.header),
                        contentScale = ContentScale.FillWidth,
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                    )
                    Row(modifier = Modifier.padding(top = 165.dp, start = 16.dp)) {
                        Image(
                            painter = painterResource(id = R.drawable.avatar),
                            contentDescription = "",
                            modifier = Modifier
                                .size(100.dp)
                        )
                    }
                }
                Row(modifier= Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Column(modifier = Modifier.weight(0.7f)) {
                        Text(
                            "Denny Kurniawan",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "@Denny",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Light
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(onClick = { /*TODO*/ }, modifier=Modifier.weight(0.3f)) {
                        Text("Follow")
                    }
                }
            }
        }
    }
}