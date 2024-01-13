package com.dennydev.wolfling.model.tweetaction.reply

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val content: String,
    val createdAt: String,
    val id: String,
    val postedBy: String,
    val repliedTo: String?=null,
    val retweetFrom: String?=null,
    val user: UserX
)