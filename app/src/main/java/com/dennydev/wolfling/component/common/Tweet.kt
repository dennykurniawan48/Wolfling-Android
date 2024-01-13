package com.dennydev.wolfling.component.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dennydev.wolfling.R
import com.dennydev.wolfling.model.listtweet.Tweet


@Composable
fun Tweet(tweet: Tweet,addLike: (tweet: Tweet) -> Unit,
          addRetweet: (tweet: Tweet) -> Unit,
          addReply: (tweet: Tweet) -> Unit,
          onProfileClick:(tweet: Tweet) -> Unit) {
    if(!tweet.retweetFrom.isNullOrEmpty() && tweet.data != null) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .border(color = Color.LightGray, width = 2.dp, shape = RoundedCornerShape(10.dp))
            .padding(6.dp)
        ) {
            Row(modifier=Modifier.fillMaxWidth()){
                Text("@${tweet.user.username} retweeted @${tweet.data.user.username}", maxLines = 1, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.labelSmall)
            }
            Spacer(modifier=Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(id = R.drawable.avatar),
                    contentDescription = "",
                    modifier = Modifier.weight(0.2f).clickable {
                        onProfileClick(tweet)
                    }
                )
                Column(
                    modifier = Modifier
                        .weight(0.8f)
                        .padding(start = 12.dp, end = 4.dp, top = 4.dp, bottom = 4.dp)
                ) {
                    Text(tweet.user.name, style = MaterialTheme.typography.titleMedium)
                    Row(){
                        Text("@${tweet.data.user.username}", style=MaterialTheme.typography.labelSmall)
                        Spacer(modifier=Modifier.width(8.dp))
                        Text(MomentAgo(tweet.createdAt), style=MaterialTheme.typography.labelSmall)
                    }
                    Spacer(modifier=Modifier.height(12.dp))
                    Text(tweet.data.content.toString(), style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }else if(!tweet.repliedTo.isNullOrEmpty() && tweet.post != null){
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .border(color = Color.LightGray, width = 2.dp, shape = RoundedCornerShape(10.dp))
            .padding(6.dp)
        ) {
            Row(modifier=Modifier.fillMaxWidth()){
                Text("@${tweet.user.username} replied @${tweet.post.user.username}", maxLines = 1, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.labelSmall)
            }
            Spacer(modifier=Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(id = R.drawable.avatar),
                    contentDescription = "",
                    modifier = Modifier.weight(0.2f).clickable {
                        onProfileClick(tweet)
                    }
                )
                Column(
                    modifier = Modifier
                        .weight(0.8f)
                        .padding(start = 12.dp, end = 4.dp, top = 4.dp, bottom = 4.dp)
                ) {
                    Text(tweet.user.name, style = MaterialTheme.typography.titleMedium)
                    Row(){
                        Text("@${ tweet.user.username }", style=MaterialTheme.typography.labelSmall)
                        Spacer(modifier=Modifier.width(8.dp))
                        Text(MomentAgo(tweet.createdAt), style=MaterialTheme.typography.labelSmall)
                    }
                    Spacer(modifier=Modifier.height(12.dp))
                    Text(tweet.content.toString(), style = MaterialTheme.typography.labelMedium)
                }
            }
            TweetActions(tweet = tweet, addLike =addLike, addRetweet, addReply)
        }
    }else{
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .border(color = Color.LightGray, width = 2.dp, shape = RoundedCornerShape(10.dp))
            .padding(6.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Image(painter = painterResource(id = R.drawable.avatar),
                    contentDescription = "",
                    modifier=Modifier.weight(0.2f).clickable {
                        onProfileClick(tweet)
                    })
                Column(modifier= Modifier
                    .weight(0.8f)
                    .padding(start = 12.dp, end = 4.dp, top = 4.dp, bottom = 4.dp)){
                    Text(tweet.user.name, style= MaterialTheme.typography.titleMedium)
                    Row(){
                        Text("@${tweet.user.username}", style=MaterialTheme.typography.labelSmall)
                        Spacer(modifier=Modifier.width(8.dp))
                        Text(MomentAgo(tweet.createdAt), style=MaterialTheme.typography.labelSmall)
                    }
                    Spacer(modifier=Modifier.height(12.dp))
                    Text(tweet.content.toString(), style=MaterialTheme.typography.labelMedium)
                }
            }
            TweetActions(tweet = tweet, addLike, addRetweet, addReply)
        }
    }
}