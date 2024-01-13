package com.dennydev.wolfling.network.common

import android.util.Log
import com.dennydev.wolfling.model.common.ApiResponse
import com.dennydev.wolfling.model.common.Constant
import com.dennydev.wolfling.model.form.LoginCredentialsForm
import com.dennydev.wolfling.model.form.ReplyTweetForm
import com.dennydev.wolfling.model.login.Login
import com.dennydev.wolfling.model.tweetaction.like.LikeAction
import com.dennydev.wolfling.model.tweetaction.reply.ReplyAction
import com.dennydev.wolfling.model.tweetaction.retweet.RetweetAction
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType

class TweetActionImpl(val client: HttpClient): TweetAction {
    override suspend fun likeAction(token: String, idTweet: String): ApiResponse<LikeAction> {
        return try{
            val response: HttpResponse = client.put(
                Constant.LIKE_URL.replace("{idTweet}", idTweet)
            ) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            }
            ApiResponse.Success(response.body())
        }catch(e: Exception){
            Log.d("Like error", e.toString())
            ApiResponse.Error("Something went wrong")
        }
    }

    override suspend fun retweetAction(token: String, idTweet: String): ApiResponse<RetweetAction> {
        return try{
            val response: HttpResponse = client.put(
                Constant.RETWEET_URL.replace("{idTweet}", idTweet)
            ) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            }
            ApiResponse.Success(response.body())
        }catch(e: Exception){
            Log.d("Retweet error", e.toString())
            ApiResponse.Error("Something went wrong")
        }
    }

    override suspend fun repliedTo(
        token: String,
        repliedTo: String,
        content: String
    ): ApiResponse<ReplyAction> {
        return try {
            val response: HttpResponse = client.post(Constant.REPLY_URL) {
                contentType(ContentType.Application.Json)
                setBody(ReplyTweetForm(content, repliedTo))
                headers{
                    append(HttpHeaders.Authorization, "Bearer $token")
            }
            }
                ApiResponse.Success(response.body())

        }catch(e: Exception){
            Log.d("error login", e.toString())
            ApiResponse.Error("Something went wrong.")
        }
    }
}