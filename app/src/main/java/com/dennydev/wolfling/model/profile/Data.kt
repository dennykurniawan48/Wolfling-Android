package com.dennydev.wolfling.model.profile

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val id: String,
    val name: String,
    val image: String? = null,
    val username: String? = null,
    val following: Boolean
)
