package com.dennydev.wolfling.model.listtweet

import kotlinx.serialization.Serializable

@Serializable
data class Tweet(
    val content: String? = null,
    val createdAt: String,
    val `data`: DataX? = null,
    val id: String,
    val likes: List<Like>,
    val post: DataX? = null,
    val postedBy: String,
    val repliedTo: String? = null,
    val retweetFrom: String?=null,
    val retweets: List<Retweet>,
    val user: UserXX,
    val retweteed: Boolean,
    val liked: Boolean
)