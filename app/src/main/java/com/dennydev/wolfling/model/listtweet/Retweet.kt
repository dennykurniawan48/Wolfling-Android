package com.dennydev.wolfling.model.listtweet

import kotlinx.serialization.Serializable

@Serializable
data class Retweet(
    val id: String,
    val user: UserX
)