package com.dennydev.wolfling.model.tweetaction.reply

import com.dennydev.wolfling.model.listtweet.Like
import com.dennydev.wolfling.model.listtweet.Retweet
import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val content: String,
    val createdAt: String,
    val `data`: Data?=null,
    val id: String,
    val likes: List<Like>,
    val post: Post,
    val postedBy: String,
    val repliedTo: String,
    val retweetFrom: String?=null,
    val retweets: List<Retweet>,
    val user: UserX
)