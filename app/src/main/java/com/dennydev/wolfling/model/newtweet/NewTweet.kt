package com.dennydev.wolfling.model.newtweet

import kotlinx.serialization.Serializable

@Serializable
data class NewTweet(
    val `data`: Data
)