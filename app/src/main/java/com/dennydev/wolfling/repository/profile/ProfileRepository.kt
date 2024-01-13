package com.dennydev.wolfling.repository.profile

import androidx.compose.runtime.Composable
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dennydev.wolfling.model.common.ApiResponse
import com.dennydev.wolfling.model.follow.Follow
import com.dennydev.wolfling.model.listtweet.Tweet
import com.dennydev.wolfling.model.profile.Profile
import com.dennydev.wolfling.network.profile.ProfileImpl
import com.dennydev.wolfling.pagingsource.HomeLatestTweet
import com.dennydev.wolfling.pagingsource.ProfileTweet
import dagger.hilt.android.scopes.ViewModelScoped
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class ProfileRepository @Inject constructor(
    val client: HttpClient
){
    fun getProfileTweet(token: String, username: String, date: String? = null): Flow<PagingData<Tweet>> {
        return Pager(PagingConfig(pageSize = 5)) {
            ProfileTweet(client,token, username, date)
        }.flow
    }
    suspend fun getProfileData(token: String, username: String): ApiResponse<Profile>{
        return ProfileImpl(client).getProfile(token, username)
    }
    suspend fun follow(token: String, userId: String): ApiResponse<Follow>{
        return ProfileImpl(client).follow(token, userId)
    }
}