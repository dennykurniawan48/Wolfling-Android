package com.dennydev.wolfling.model.login

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val accessToken: String,
    val bio: String? = null,
    val email: String,
    val emailVerified: String?=null,
    val expiresIn: Int,
    val google: Boolean,
    val id: String,
    val image: String?=null,
    val name: String,
    val username: String?=null
)