package com.dennydev.wolfling.model.listtweet

import kotlinx.serialization.Serializable

@Serializable
data class UserX(
    val id: String,
    val username: String
)