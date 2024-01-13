package com.dennydev.wolfling.model.detailtweet

import kotlinx.serialization.Serializable

@Serializable
data class Tweet(
    val id: String,
    val content: String,
    val createdAt: String,
    val liked: Boolean,
    val likes: List<Like>,
    val retweeted: Boolean,
    val retweets: List<Retweet>,
    val user: UserXX
)