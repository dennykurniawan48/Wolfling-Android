package com.dennydev.wolfling.model.newtweet

import com.dennydev.wolfling.model.listtweet.Retweet
import com.dennydev.wolfling.model.listtweet.UserX
import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val content: String,
    val createdAt: String,
    val `data`: Data? =null,
    val id: String,
    val liked: Boolean,
    val likes: List<UserX>,
    val post: Data? = null,
    val postedBy: String,
    val repliedTo: String?=null,
    val retweetFrom: String?=null,
    val retweeted: Boolean,
    val retweets: List<Retweet>,
    val user: User
)