package com.dennydev.wolfling.network.profile

import com.dennydev.wolfling.model.common.ApiResponse
import com.dennydev.wolfling.model.common.Constant
import com.dennydev.wolfling.model.follow.Follow
import com.dennydev.wolfling.model.form.NewTweetForm
import com.dennydev.wolfling.model.listtweet.ListTweet
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.put
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders

class ProfileImpl(val client: HttpClient): Profile {
    override suspend fun getProfile(token: String, username: String): ApiResponse<com.dennydev.wolfling.model.profile.Profile> {
        return try {
            val response: HttpResponse = client.get(
                Constant.PROFILE_URL.replace("{username}", username)
            ){
                headers{
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            }
            ApiResponse.Success(response.body())
        }catch(e: Exception){
            ApiResponse.Error("Something went wrong.")
        }
    }

    override suspend fun getProfileTweet(token: String, username: String, page: Int, date: String?): ListTweet {
        val response: HttpResponse = client.get(
            if(date == null)
                Constant.PROFILE_TWEET.replace("{username}", username).replace("{page}", page.toString())
            else
                Constant.PROFILE_TWEET.replace("{username}", username).replace("{page}", page.toString())+"&date=$date"
        ){
            headers{
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
        return response.body()
    }

    override suspend fun follow(token: String, userId: String): ApiResponse<Follow> {
        return try {
            val response: HttpResponse = client.put(
                Constant.FOLLOW_URL.replace("{id}", userId)
            ){
                headers{
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            }
            ApiResponse.Success(response.body())
        }catch(e: Exception){
            ApiResponse.Error("Something went wrong.")
        }
    }
}