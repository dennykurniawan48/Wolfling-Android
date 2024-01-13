package com.dennydev.wolfling.model.tweetaction.retweet

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val content: String?=null,
    val createdAt: String,
    val id: String,
    val postedBy: String,
    val repliedTo: String?=null,
    val retweetFrom: String,
    val user: User
)