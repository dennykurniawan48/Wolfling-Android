package com.dennydev.wolfling.network.home.newtweet

import com.dennydev.wolfling.model.common.ApiResponse
import com.dennydev.wolfling.model.newtweet.NewTweet

interface NewTweet {
    suspend fun addTweet(token: String, content: String): ApiResponse<NewTweet>
}