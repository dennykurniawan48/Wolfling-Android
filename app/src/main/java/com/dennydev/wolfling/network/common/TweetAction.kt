package com.dennydev.wolfling.network.common

import com.dennydev.wolfling.model.common.ApiResponse
import com.dennydev.wolfling.model.tweetaction.like.LikeAction
import com.dennydev.wolfling.model.tweetaction.reply.ReplyAction
import com.dennydev.wolfling.model.tweetaction.retweet.RetweetAction

interface TweetAction {
    suspend fun likeAction(token: String, idTweet: String) : ApiResponse<LikeAction>
    suspend fun retweetAction(token: String, idTweet: String) : ApiResponse<RetweetAction>
    suspend fun repliedTo(token: String, repliedTo: String, content: String) : ApiResponse<ReplyAction>
}