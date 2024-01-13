package com.dennydev.wolfling.model.detailtweet

import kotlinx.serialization.Serializable

@Serializable
data class UserXX(
    val id: String,
    val image: String?=null,
    val name: String,
    val username: String?=null
)