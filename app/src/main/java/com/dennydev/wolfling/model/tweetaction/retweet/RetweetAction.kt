package com.dennydev.wolfling.model.tweetaction.retweet

import kotlinx.serialization.Serializable

@Serializable
data class RetweetAction(
    val `data`: Data
)