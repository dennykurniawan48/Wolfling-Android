package com.dennydev.wolfling.pagingsource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dennydev.wolfling.model.listtweet.ListTweet
import com.dennydev.wolfling.model.listtweet.Tweet
import com.dennydev.wolfling.network.home.latesttweet.LatestTweetImpl
import com.dennydev.wolfling.network.profile.ProfileImpl
import io.ktor.client.HttpClient

class ProfileTweet(
    val client: HttpClient,
    val token: String,
    val username: String,
    var date: String?
): PagingSource<Int, Tweet>() {
    override fun getRefreshKey(state: PagingState<Int, Tweet>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Tweet> {
        return try {
            val page = params.key ?: 1
            val response: ListTweet = ProfileImpl(client).getProfileTweet(token, username, page, date)
            this.date = response.data.date
            LoadResult.Page(
                data = response.data.tweet,
                prevKey = if(page==1) null else page-1,
                nextKey = if(page==response.data.totalPage) null else page+1
            )
        } catch (e: Exception) {
            Log.d("data", e.message.toString())
            LoadResult.Error(e)
        }
    }
}