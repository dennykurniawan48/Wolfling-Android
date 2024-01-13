package com.dennydev.wolfling.model.common

data class TweetStatus(
    val id: String,
    var liked: Boolean,
    var retweeted: Boolean
)
