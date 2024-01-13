package com.dennydev.wolfling.network.profile

import com.dennydev.wolfling.model.common.ApiResponse
import com.dennydev.wolfling.model.follow.Follow
import com.dennydev.wolfling.model.listtweet.ListTweet
import com.dennydev.wolfling.model.profile.Profile

interface Profile {
    suspend fun getProfile(token: String, username: String): ApiResponse<Profile>
    suspend fun getProfileTweet(token: String, username: String, page: Int, date: String?): ListTweet
    suspend fun follow(token: String, userId: String): ApiResponse<Follow>
}