package com.dennydev.wolfling.component.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dennydev.wolfling.model.listtweet.Tweet

@Composable
fun TweetActions(
    tweet: Tweet,
    addLike: (tweet: Tweet) -> Unit,
    addRetweet: (tweet: Tweet) -> Unit,
    addReply: (tweet: Tweet) -> Unit
) {
    Row(modifier = Modifier) {
        Spacer(modifier = Modifier.weight(0.25f))
        Row(modifier = Modifier.weight(0.75f), horizontalArrangement = Arrangement.SpaceAround) {
            IconButton(onClick = {
                addReply(tweet)
            }) {
                Icon(imageVector = Icons.Default.ChatBubbleOutline, contentDescription = "reply")
            }
            IconButton(onClick = {
                addRetweet(tweet)
            }) {
                Row() {
                    if (tweet.retweteed) {
                        Icon(
                            imageVector = Icons.Default.Repeat,
                            contentDescription = "retweet",
                            tint = Color.Green
                        )
                    } else {
                        Icon(imageVector = Icons.Default.Repeat, contentDescription = "retweet")
                    }
                    if(tweet.retweets.isNotEmpty()) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Text((tweet.retweets.size).toString())
                    }
                }
            }
            IconButton(onClick = {
                addLike(tweet)
            }) {
                Row {
                    if (tweet.liked) {
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
                    if(tweet.likes.isNotEmpty()) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(tweet.likes.size.toString())
                    }
                }
            }
        }
    }
}