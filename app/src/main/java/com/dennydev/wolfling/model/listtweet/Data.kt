package com.dennydev.wolfling.model.listtweet

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val currentPage: Int,
    val date: String,
    val total: Int,
    val totalPage: Int,
    val tweet: List<Tweet>
)