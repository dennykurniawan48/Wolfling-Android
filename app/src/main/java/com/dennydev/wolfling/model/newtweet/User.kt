package com.dennydev.wolfling.model.newtweet

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val image: String?=null,
    val name: String,
    val username: String?=null
)