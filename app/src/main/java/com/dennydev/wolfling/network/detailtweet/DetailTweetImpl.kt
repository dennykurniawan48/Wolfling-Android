package com.dennydev.wolfling.network.detailtweet

import android.util.Log
import com.dennydev.wolfling.model.common.ApiResponse
import com.dennydev.wolfling.model.common.Constant
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders

class DetailTweetImpl(val client: HttpClient): DetailTweet {
    override suspend fun getTweet(
        token: String,
        idTweet: String,
    ): ApiResponse<com.dennydev.wolfling.model.detailtweet.DetailTweet> {
        return try{
            val response: HttpResponse = client.get(
                    Constant.DETAIL_TWEET_URL.replace("{idTweet}", idTweet)
            ) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            }
            ApiResponse.Success(response.body())
        }catch(e: Exception){
            Log.d("error detail", e.toString())
            ApiResponse.Error("Something wen't wrong")
        }
    }
}