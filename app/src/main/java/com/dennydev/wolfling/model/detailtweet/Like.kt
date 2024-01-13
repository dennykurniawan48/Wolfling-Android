package com.dennydev.wolfling.model.detailtweet

import kotlinx.serialization.Serializable

@Serializable
data class Like(
    val user: User
)