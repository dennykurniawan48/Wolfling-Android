package com.dennydev.wolfling.model.tweetaction.reply

import kotlinx.serialization.Serializable

@Serializable
data class UserX(
    val id: String,
    val image: String?=null,
    val name: String,
    val username: String
)