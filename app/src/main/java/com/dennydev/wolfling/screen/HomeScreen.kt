package com.dennydev.wolfling.screen

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.BottomNavigation
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.dennydev.wolfling.R
import com.dennydev.wolfling.component.BottomNav
import com.dennydev.wolfling.component.common.LoadingTweet
import com.dennydev.wolfling.component.common.Tweet
import com.dennydev.wolfling.component.home.EmptyResult
import com.dennydev.wolfling.model.common.ApiResponse
import com.dennydev.wolfling.model.listtweet.Tweet
import com.dennydev.wolfling.navigation.Screen
import com.dennydev.wolfling.viewmodel.HomeViewModel
import com.dennydev.wolfling.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(navController: NavHostController,
               mainViewModel: MainViewModel,
               homeViewModel: HomeViewModel,
               notificationCount: Int) {
    val selectedBottomIndex by mainViewModel.selecteBottomNav
    val tweets = homeViewModel.tweets.collectAsLazyPagingItems()
    val firstLoad by homeViewModel.firstLoad
    val token by homeViewModel.token.collectAsState(initial = "")
    val username by homeViewModel.username.collectAsState(initial = "")
    val isUsernameSet by homeViewModel.isUsernameSet
    val tweetStatus by homeViewModel.tweetStatus.collectAsState()

    LaunchedEffect(key1 = tweetStatus){
        Log.d("status", tweetStatus.toString())
    }

    val refreshing by homeViewModel.isRefresh.collectAsState()

    val pullRefreshState = rememberPullRefreshState(refreshing, {
        homeViewModel.loadItems(token)
        homeViewModel.getNotification(token)
    })

    fun addLike(tweet: Tweet){
        homeViewModel.likeAction(tweet)
    }

    fun addRetweet(tweet: Tweet){
        homeViewModel.retweetAction(tweet)
    }

    fun addReply(tweet: Tweet){
        navController.navigate(Screen.DetailTweetScreen.route.replace("{id}", tweet.id))
    }

    LaunchedEffect(key1 = isUsernameSet,key2=token){
        if(!isUsernameSet && token.isNotEmpty()){
            navController.navigate(Screen.SetUsernameScreen.route){
                popUpTo(Screen.SetUsernameScreen.route){
                    inclusive = true
                }
            }
        }
    }

    LaunchedEffect(key1 = firstLoad, token){
        if(firstLoad && token.isNotEmpty()){
            homeViewModel.loadItems(token)
            homeViewModel.changeFirstLoad()
            homeViewModel.getNotification(token)
            homeViewModel.connectPusher()
        }
    }

    Scaffold(modifier = Modifier
        .fillMaxSize(), topBar = {
        TopAppBar(title = { 
            Text(text = "All tweets")
        })
    }, floatingActionButton = {
        FloatingActionButton(onClick = {
            navController.navigate(Screen.NewTweetScreen.route)
        }) {
            Icon(imageVector = Icons.Default.Create, contentDescription = "")
        }
    }, floatingActionButtonPosition = FabPosition.End, bottomBar = {
        BottomNav(
            navController = navController,
            selectedBottomIndex = selectedBottomIndex,
            notificationCount=notificationCount
        ){
            mainViewModel.onChangeSelectedBottomNav(it)
        }
    }){ values ->
            LazyColumn(modifier= Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
                .padding(values),
                horizontalAlignment = Alignment.CenterHorizontally){

                item{
                    if(refreshing) {
                        PullRefreshIndicator(
                            refreshing = refreshing,
                            state = pullRefreshState,
                        )
                    }
                }

                    if (tweets.loadState.refresh is LoadState.NotLoading && tweets.itemCount > 0) {
                        items(tweets.itemCount) { item ->
                            tweets.get(item)?.let {
                                Tweet(tweet = it, addLike = {
                                      addLike(it)
                                }, addRetweet = {
                                    addRetweet(it)
                                }, addReply = {
                                    addReply(it)
                                }, onProfileClick = {
                                    if(it.user.username == username){
                                        navController.navigate(Screen.ProfileScreen.route)
                                    }else{
                                        navController.navigate(Screen.UserProfileScreen.route.replace("{id}", it.user.username.toString()))
                                    }
                                })
//                                Spacer(modifier = Modifier.height(24.dp))
                            }
                        }

                        item{
                            if(tweets.loadState.append is LoadState.Loading && !tweets.loadState.append.endOfPaginationReached){
                                LoadingTweet()
                            }
                        }
                    }else if (tweets.loadState.refresh is LoadState.NotLoading && tweets.loadState.append is LoadState.NotLoading && !tweets.loadState.refresh.endOfPaginationReached && tweets.itemCount == 0) {
                        item {
                            Spacer(modifier=Modifier.height(24.dp))
                            EmptyResult()
                        }
                    } else if (tweets.loadState.refresh is LoadState.NotLoading && tweets.loadState.append is LoadState.Loading && !tweets.loadState.refresh.endOfPaginationReached && tweets.itemCount == 0) {
                        item {
                            Spacer(modifier=Modifier.height(48.dp))
                            Text("Empty result")
                        }
                    } else if (tweets.loadState.refresh is LoadState.Loading) {
                        item{
                            LoadingTweet()
                        }
                    }

                }
            }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun PrevHome() {
    Scaffold(modifier = Modifier
        .fillMaxSize(), topBar = {

    }, floatingActionButton = {
        FloatingActionButton(onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Default.Create, contentDescription = "")
        }
    }, floatingActionButtonPosition = FabPosition.End){
        Column(modifier=Modifier.padding(it)) {

        }
    }
}