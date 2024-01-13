package com.dennydev.wolfling.network.home.latesttweet

import android.util.Log
import com.dennydev.wolfling.model.common.ApiResponse
import com.dennydev.wolfling.model.common.Constant
import com.dennydev.wolfling.model.listtweet.ListTweet
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders

class LatestTweetImpl(private val client: HttpClient) : LatestTweet {
    override suspend fun getLatestTweet(token: String, page: Int, date: String?): ListTweet {
        val response: HttpResponse = client.get(
            if (date == null)
                Constant.LATEST_HOME_TWEET.replace("{page}", page.toString())
            else
                Constant.LATEST_HOME_TWEET.replace("{page}", page.toString()) + "&date=$date"
        ) {
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
        return response.body()
    }
}