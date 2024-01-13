package com.dennydev.wolfling.repository.detailtweet

import com.dennydev.wolfling.model.common.ApiResponse
import com.dennydev.wolfling.model.detailtweet.DetailTweet
import com.dennydev.wolfling.network.detailtweet.DetailTweetImpl
import dagger.hilt.android.scopes.ViewModelScoped
import io.ktor.client.HttpClient
import javax.inject.Inject

@ViewModelScoped
class DetailTweetRepository @Inject constructor(
    private val client: HttpClient
) {
    suspend fun getTweet(token: String, idTweet: String): ApiResponse<DetailTweet>{
        return DetailTweetImpl(client).getTweet(token, idTweet)
    }
}