package com.dennydev.wolfling.network.home.latesttweet

import com.dennydev.wolfling.model.common.ApiResponse
import com.dennydev.wolfling.model.listtweet.ListTweet

interface LatestTweet {
    suspend fun getLatestTweet(token: String, page: Int, date: String?): ListTweet
}