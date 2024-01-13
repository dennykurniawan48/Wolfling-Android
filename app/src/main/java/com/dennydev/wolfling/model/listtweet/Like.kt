package com.dennydev.wolfling.model.listtweet

import kotlinx.serialization.Serializable

@Serializable
data class Like(
    val id: String,
    val userId: String
)