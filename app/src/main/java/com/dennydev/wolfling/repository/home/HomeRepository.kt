package com.dennydev.wolfling.repository.home

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dennydev.wolfling.model.common.ApiResponse
import com.dennydev.wolfling.model.detailnotification.DetailNotification
import com.dennydev.wolfling.model.listtweet.Tweet
import com.dennydev.wolfling.model.newtweet.NewTweet
import com.dennydev.wolfling.model.notification.NotificationCount
import com.dennydev.wolfling.model.register.Register
import com.dennydev.wolfling.model.tweetaction.like.LikeAction
import com.dennydev.wolfling.model.tweetaction.reply.ReplyAction
import com.dennydev.wolfling.model.tweetaction.retweet.RetweetAction
import com.dennydev.wolfling.network.common.TweetActionImpl
import com.dennydev.wolfling.network.home.newtweet.NewTweetImpl
import com.dennydev.wolfling.network.home.setusername.SetUsernameImpl
import com.dennydev.wolfling.network.notification.Notificationimpl
import com.dennydev.wolfling.pagingsource.HomeLatestTweet
import dagger.hilt.android.scopes.ViewModelScoped
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class HomeRepository @Inject constructor(
    private val client: HttpClient
) {
    fun getLatestTweet(token: String, date: String? = null): Flow<PagingData<Tweet>> {
        return Pager(PagingConfig(pageSize = 5)) {
            HomeLatestTweet(client, token, date)
        }.flow
    }

    suspend fun setNewUsername(token: String, newUsername: String): ApiResponse<Register>{
        return SetUsernameImpl(client).setUsername(token, newUsername)
    }

    suspend fun sendNewTweet(token: String, content: String): ApiResponse<NewTweet>{
        return NewTweetImpl(client).addTweet(token, content)
    }

    suspend fun like(token: String, idTweet: String): ApiResponse<LikeAction>{
        return TweetActionImpl(client).likeAction(token, idTweet)
    }

    suspend fun retweet(token: String, idTweet: String): ApiResponse<RetweetAction>{
        return TweetActionImpl(client).retweetAction(token, idTweet)
    }

    suspend fun getNotificationCount(token: String): ApiResponse<NotificationCount>{
        return Notificationimpl(client).getNotificationCount(token)
    }

    suspend fun getNotificationDetail(token: String): ApiResponse<DetailNotification>{
        return Notificationimpl(client).getNotificationDetail(token)
    }

    suspend fun reply(token: String, idTweet: String, content: String): ApiResponse<ReplyAction>{
        return TweetActionImpl(client).repliedTo(token, idTweet, content)
    }
}