package com.dennydev.wolfling.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.dennydev.wolfling.R
import com.dennydev.wolfling.component.BottomNav
import com.dennydev.wolfling.component.common.MomentAgo
import com.dennydev.wolfling.component.common.Tweet
import com.dennydev.wolfling.component.common.TweetActions
import com.dennydev.wolfling.model.common.ApiResponse
import com.dennydev.wolfling.model.detailtweet.Tweet
import com.dennydev.wolfling.navigation.Screen
import com.dennydev.wolfling.viewmodel.DetailTweetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTweetScreen(
    navController: NavHostController,
    idTweet: String
) {
    val detailTweetViewModel: DetailTweetViewModel = hiltViewModel(key=idTweet)

    val token by detailTweetViewModel.token.collectAsState(initial = "")
    val detailTweet by detailTweetViewModel.detailTweet
    val mainTweet by detailTweetViewModel.mainTweet
    val replies = detailTweetViewModel.replies
    val replyMessage by detailTweetViewModel.replyMessage
    val replyResponse by detailTweetViewModel.replyResponse

    fun addLike(tweet: Tweet){
        detailTweetViewModel.likeAction(tweet)
    }

    fun addRetweet(tweet: Tweet){
        detailTweetViewModel.retweetAction(tweet)
    }

    fun addReply(tweet: Tweet){
        navController.navigate(Screen.DetailTweetScreen.route.replace("{id}", tweet.id))
    }

    LaunchedEffect(key1 = replies){
        Log.d("replies", replies.toString())
    }
    
    LaunchedEffect(key1 = token){
        detailTweetViewModel.getDetailTweet(token, idTweet)
    }

    Scaffold(modifier = Modifier
        .fillMaxSize(), topBar = {
        TopAppBar(title = {
            Text("Detail Tweet")
        }, navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Default.ArrowBackIos, contentDescription = "")
            }
        })
    }) { values ->
        LazyColumn(modifier= Modifier
            .fillMaxSize()
            .padding(values)
            .padding(16.dp)){
            if(detailTweet is ApiResponse.Loading){
                item{
                    Column(modifier=Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally){
                        Spacer(modifier = Modifier.height(24.dp))
                        Text("Loading Tweet...")
                    }
                }
            }
            detailTweet.data?.data?.let {
                item {
                    mainTweet?.let {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Image(
                                    painter = painterResource(id = R.drawable.avatar),
                                    contentDescription = "",
                                    modifier = Modifier.weight(0.25f)
                                )
                                Column(
                                    modifier = Modifier
                                        .weight(0.75f)
                                        .padding(
                                            start = 12.dp,
                                            end = 4.dp,
                                            top = 4.dp,
                                            bottom = 4.dp
                                        )
                                ) {
                                    Text(
                                        it.user?.name ?: "",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Row() {
                                        Text(
                                            it.user.username.toString(),
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            MomentAgo(it.createdAt),
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        it.content,
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                }
                            }
                            Row(modifier = Modifier) {
                                Spacer(modifier = Modifier.weight(0.25f))
                                Row(
                                    modifier = Modifier.weight(0.75f),
                                    horizontalArrangement = Arrangement.SpaceAround
                                ) {
                                    IconButton(onClick = { /*TODO*/ }, enabled = false) {
                                        Icon(
                                            imageVector = Icons.Default.ChatBubbleOutline,
                                            contentDescription = "reply"
                                        )
                                    }
                                    IconButton(onClick = {
                                    addRetweet(it)
                                    }) {
                                        Row {
                                            if (it.retweeted) {
                                                Icon(
                                                    imageVector = Icons.Default.Repeat,
                                                    contentDescription = "retweet",
                                                    tint = Color.Green
                                                )
                                            } else {
                                                Icon(
                                                    imageVector = Icons.Default.Repeat,
                                                    contentDescription = "retweet"
                                                )
                                            }
                                            if (it.retweets.isNotEmpty()) {
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(it.retweets.size.toString())
                                            }
                                        }
                                    }
                                    IconButton(onClick = {
                                    addLike(it)
                                    }) {
                                        Row {
                                            if (it.liked) {
                                                Icon(
                                                    imageVector = Icons.Default.Favorite,
                                                    contentDescription = "Like",
                                                    tint = Color.Red
                                                )
                                            } else {
                                                Icon(
                                                    imageVector = Icons.Default.FavoriteBorder,
                                                    contentDescription = "Like"
                                                )
                                            }
                                            if (it.likes.isNotEmpty()) {
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(it.likes.size.toString())
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                item {
                    Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
                        TextField(value = replyMessage, onValueChange = {
                             detailTweetViewModel.updateReplyMessage(it)
                        }, modifier = Modifier.weight(0.85f), maxLines = 6,
                            enabled = replyResponse !is ApiResponse.Loading,
                            isError = replyResponse is ApiResponse.Error
                        )
                        IconButton(onClick = {
                              detailTweetViewModel.replyAction(token, idTweet ,replyMessage)
                        }, enabled = replyMessage.isNotEmpty() || replyResponse is ApiResponse.Loading, modifier = Modifier.weight(0.15f)) {
                            Icon(imageVector = Icons.Default.Send, contentDescription = "Send")
                        }
                    }
                }
                items(items=replies){
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Image(
                                painter = painterResource(id = R.drawable.avatar),
                                contentDescription = "",
                                modifier = Modifier.weight(0.25f)
                            )
                            Column(
                                modifier = Modifier
                                    .weight(0.75f)
                                    .padding(
                                        start = 12.dp,
                                        end = 4.dp,
                                        top = 4.dp,
                                        bottom = 4.dp
                                    )
                            ) {
                                Text(
                                    it.user.name,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Row() {
                                    Text(
                                        it.user.username.toString(),
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        MomentAgo(it.createdAt),
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    it.content,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        }
                        Row(modifier = Modifier) {
                            Spacer(modifier = Modifier.weight(0.25f))
                            Row(
                                modifier = Modifier.weight(0.75f),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                IconButton(onClick = {
                                    addReply(it)
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.ChatBubbleOutline,
                                        contentDescription = "reply"
                                    )
                                }
                                IconButton(onClick = {
                                    addRetweet(it)
                                }) {
                                    if (it.retweeted) {
                                        Icon(
                                            imageVector = Icons.Default.Repeat,
                                            contentDescription = "retweet",
                                            tint = Color.Green
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.Repeat,
                                            contentDescription = "retweet"
                                        )
                                    }
                                }
                                IconButton(onClick = {
                                    addLike(it)
                                }) {
                                    if (it.liked) {
                                        Icon(
                                            imageVector = Icons.Default.Favorite,
                                            contentDescription = "Like",
                                            tint = Color.Red
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.FavoriteBorder,
                                            contentDescription = "Like"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}