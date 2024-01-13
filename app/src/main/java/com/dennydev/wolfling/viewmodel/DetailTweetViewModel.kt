package com.dennydev.wolfling.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dennydev.wolfling.model.common.ApiResponse
import com.dennydev.wolfling.model.common.TweetStatus
import com.dennydev.wolfling.model.detailtweet.DetailTweet
import com.dennydev.wolfling.model.detailtweet.Tweet
import com.dennydev.wolfling.model.detailtweet.UserXX
import com.dennydev.wolfling.model.tweetaction.reply.ReplyAction
import com.dennydev.wolfling.repository.AuthStoreRepository
import com.dennydev.wolfling.repository.detailtweet.DetailTweetRepository
import com.dennydev.wolfling.repository.home.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailTweetViewModel @Inject constructor(
    private val repository: DetailTweetRepository,
    private val homeRepository: HomeRepository,
    private val authStoreRepository: AuthStoreRepository
): ViewModel() {
    val token = authStoreRepository.flowToken
    private val _detailTweet = mutableStateOf<ApiResponse<DetailTweet>>(ApiResponse.Idle())
    private val _maintweet = mutableStateOf<Tweet?>(null)
    val mainTweet: State<Tweet?> = _maintweet

    private val _replyMessage = mutableStateOf("")
    val replyMessage: State<String> = _replyMessage

    private val _replies = mutableStateListOf<Tweet>()
    val replies: List<Tweet> = _replies
    private val _replyResponse = mutableStateOf<ApiResponse<ReplyAction>>(ApiResponse.Idle())
    val replyResponse: State<ApiResponse<ReplyAction>> = _replyResponse


    val detailTweet: State<ApiResponse<DetailTweet>> = _detailTweet

    fun updateReplyMessage(it: String){
        if(it.length <= 70) {
            _replyMessage.value = it
        }
    }

    fun likeAction(tweet: Tweet){
        viewModelScope.launch(Dispatchers.IO) {

            if(tweet.id == _maintweet.value?.id){
                _maintweet.value?.let {
                    _maintweet.value = it.copy(
                        liked = !it.liked
                    )
                }
            }else {
                val currentList = _replies.toMutableList()
                val indexTweetInTemporary = currentList.indexOfFirst { it.id == tweet.id }
                if (indexTweetInTemporary != -1) {
                    withContext(Dispatchers.Main) {
                        currentList[indexTweetInTemporary] = currentList[indexTweetInTemporary].copy(
                            liked = !_replies[indexTweetInTemporary].liked
                        )
                        _replies[indexTweetInTemporary] = currentList[indexTweetInTemporary]
                    }
                }

            }
            token.collectLatest {
                homeRepository.like( it, tweet.id)
            }
        }
    }

    fun retweetAction(tweet: Tweet){
        viewModelScope.launch(Dispatchers.IO) {
            if(tweet.id == _maintweet.value?.id){
                _maintweet.value?.let {
                    _maintweet.value = it.copy(
                        retweeted = !it.retweeted
                    )
                }
            }else {
                val currentList = _replies.toMutableList()
                val indexTweetInTemporary = currentList.indexOfFirst { it.id == tweet.id }
                if (indexTweetInTemporary != -1) {
                    withContext(Dispatchers.Main) {
                        currentList[indexTweetInTemporary] = currentList[indexTweetInTemporary].copy(
                            retweeted = !_replies[indexTweetInTemporary].retweeted
                        )
                        _replies[indexTweetInTemporary] = currentList[indexTweetInTemporary]
                    }
                }
            }
            token.collectLatest {
                homeRepository.retweet( it, tweet.id)
            }
        }
    }

    fun replyAction(token: String, idTweet: String,replyMessage: String){
        _replyResponse.value = ApiResponse.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            val response = homeRepository.reply(token, idTweet, replyMessage)
            if(response is ApiResponse.Success){
                _replyMessage.value = ""
                response.data?.data?.let{
                    val data = Tweet(
                        id = it.id,
                        content = it.content,
                        createdAt = it.createdAt,
                        liked = false,
                        retweeted = false,
                        likes = emptyList(),
                        retweets = emptyList(),
                        user = UserXX(
                            id=it.user.id,
                            image = it.user.image,
                            name=it.user.name,
                            username = it.user.username
                        )
                    )
                    _replies.add(0, data)
                }
            }
            _replyResponse.value = response
        }
    }

    fun getDetailTweet(token: String, idTweet: String){
        _detailTweet.value = ApiResponse.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getTweet(token, idTweet)
            if(response is ApiResponse.Success){
                response.data?.data?.let {
                    _maintweet.value = it.tweet
                    _replies.clear()
                    _replies.addAll(it.replies)
                }
            }
            _detailTweet.value = response
        }
    }
}