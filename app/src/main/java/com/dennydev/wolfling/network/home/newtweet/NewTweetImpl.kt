package com.dennydev.wolfling.network.home.newtweet

import android.util.Log
import com.dennydev.wolfling.model.common.ApiResponse
import com.dennydev.wolfling.model.common.Constant
import com.dennydev.wolfling.model.form.LoginCredentialsForm
import com.dennydev.wolfling.model.form.NewTweetForm
import com.dennydev.wolfling.model.listtweet.Tweet
import com.dennydev.wolfling.model.login.Login
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType

class NewTweetImpl(val client: HttpClient): NewTweet {
    override suspend fun addTweet(token: String, content: String): ApiResponse<com.dennydev.wolfling.model.newtweet.NewTweet> {
        return try {
            val response: HttpResponse = client.post(Constant.TWEET_URL) {
                contentType(ContentType.Application.Json)
                setBody(NewTweetForm(content))
                headers{
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            }
            ApiResponse.Success(response.body())
        }catch(e: Exception){
            Log.d("new tweet error", e.toString())
            ApiResponse.Error("Something went wrong.")
        }
    }
}