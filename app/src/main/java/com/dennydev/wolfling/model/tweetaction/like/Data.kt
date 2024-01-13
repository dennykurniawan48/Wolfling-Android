package com.dennydev.wolfling.model.tweetaction.like

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val id: String,
    val postId: String,
    val userId: String
)