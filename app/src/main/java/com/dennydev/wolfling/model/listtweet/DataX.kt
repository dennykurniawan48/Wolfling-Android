package com.dennydev.wolfling.model.listtweet

import kotlinx.serialization.Serializable

@Serializable
data class DataX(
    val content: String? = null,
    val createdAt: String,
    val id: String,
    val postedBy: String,
    val repliedTo: String? = null,
    val retweetFrom: String? = null,
    val user: UserXX,
    val post: DataX? = null,
    val retweet: Retweet? = null
)