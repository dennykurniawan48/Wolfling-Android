package com.dennydev.wolfling.network.detailtweet

import com.dennydev.wolfling.model.common.ApiResponse
import com.dennydev.wolfling.model.detailtweet.DetailTweet

interface DetailTweet {
    suspend fun getTweet(idTweet: String, token: String): ApiResponse<DetailTweet>
}