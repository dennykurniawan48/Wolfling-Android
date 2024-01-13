package com.dennydev.wolfling.model.register

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val bio: String?=null,
    val email: String,
    val emailVerified: String?=null,
    val id: String,
    val image: String?=null,
    val name: String,
    val username: String?=null
)