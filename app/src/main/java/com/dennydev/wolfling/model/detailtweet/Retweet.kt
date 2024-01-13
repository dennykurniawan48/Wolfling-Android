package com.dennydev.wolfling.model.detailtweet

import kotlinx.serialization.Serializable

@Serializable
data class Retweet(
    val user: User
)