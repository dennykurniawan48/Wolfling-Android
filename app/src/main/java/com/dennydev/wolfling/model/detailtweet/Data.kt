package com.dennydev.wolfling.model.detailtweet

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val replies: List<Tweet>,
    val tweet: Tweet
)