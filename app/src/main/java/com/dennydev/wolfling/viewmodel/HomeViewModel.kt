package com.dennydev.wolfling.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.dennydev.wolfling.model.common.ApiResponse
import com.dennydev.wolfling.model.common.Constant
import com.dennydev.wolfling.model.common.TweetStatus
import com.dennydev.wolfling.model.listtweet.Tweet
import com.dennydev.wolfling.model.newtweet.NewTweet
import com.dennydev.wolfling.model.register.Register
import com.dennydev.wolfling.repository.AuthStoreRepository
import com.dennydev.wolfling.repository.home.HomeRepository
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository,
    private val authStoreRepository: AuthStoreRepository
): ViewModel() {
    val token = authStoreRepository.flowToken
    val username = authStoreRepository.flowUsername
    val userId = authStoreRepository.flowUserId

    private val _date: MutableState<String?> = mutableStateOf(null)
    val tweetStatus = MutableStateFlow(emptyList<TweetStatus>())

    private val _isUsernameSet = mutableStateOf(true)
    val isUsernameSet: State<Boolean> = _isUsernameSet

    private val _tweets = MutableStateFlow<PagingData<Tweet>>(
        PagingData.empty(
            sourceLoadStates = LoadStates(
                refresh = LoadState.NotLoading(endOfPaginationReached = false),
                prepend = LoadState.NotLoading(endOfPaginationReached = false),
                append = LoadState.NotLoading(endOfPaginationReached = false)
            )
        )
    )
    val tweets: StateFlow<PagingData<Tweet>> = _tweets
    private val _firstLoad = mutableStateOf(true)
    val firstLoad: State<Boolean> = _firstLoad
    private val _changeUsernameResponse = mutableStateOf<ApiResponse<Register>>(ApiResponse.Idle())
    val changeUsernameResponse: State<ApiResponse<Register>> = _changeUsernameResponse

    private val _newUsername = mutableStateOf("")
    val newUsername: State<String> = _newUsername

    private val _isValidNewTweetForm = mutableStateOf(false)
    val isValidNewTweetForm: State<Boolean> = _isValidNewTweetForm

    private val _newTweetresponse = mutableStateOf<ApiResponse<NewTweet>>(ApiResponse.Idle())
    val newTweetResponse: State<ApiResponse<NewTweet>> = _newTweetresponse

    private val _newTweet = mutableStateOf("")
    val newTweet: State<String> = _newTweet

    private val _isRefresh = MutableStateFlow(false)
    val isRefresh: StateFlow<Boolean>
        get() = _isRefresh.asStateFlow()

    private val pusherBind = mutableStateOf(false)

    val options = PusherOptions().setCluster(Constant.PUSHER_CLUSTER)
    val pusher = Pusher(Constant.PUSHER_KEY, options)

    private val _newNotification = mutableStateOf(0)
    val newNotification: State<Int> = _newNotification

    fun changeTweet(tweet: String) {
        _isValidNewTweetForm.value = tweet.isNotEmpty()
        if (tweet.length <= 225) {
            _newTweet.value = tweet
        }
    }

    init {
        viewModelScope.launch {
            authStoreRepository.flowUsername.collect { username ->
                _isUsernameSet.value = username.isNotEmpty()
            }
        }
    }

    fun getNotification(token: String){
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getNotificationCount(token)
            if(response is ApiResponse.Success){
                response.data?.data?.let {
                    _newNotification.value = it.count
                }
            }
        }
    }

    fun connectPusher() {
        viewModelScope.launch {
            authStoreRepository.flowUserId.collect {
                if(!pusherBind.value) {
                    val channel = pusher.subscribe("user__${it}__notification")

                    channel.bind("new_notification") { event ->
                        Log.d("enter", "enter notification")
                        val data = event.data // Process event data as needed
                        _newNotification.value = _newNotification.value + 1
                    }

                    channel.bind("clear_notification") { event ->
                        val data = event.data // Process event data as needed
                        _newNotification.value = 0
                    }

                    pusher.connect()
                    pusherBind.value = true
                }
            }
        }
    }


    fun logout(){
        disconnectPusher()
        viewModelScope.launch {
            authStoreRepository.removeToken()
            _firstLoad.value = false
        }
    }

    fun disconnectPusher() {
        viewModelScope.launch {
            authStoreRepository.flowUserId.collect {
                pusher.unsubscribe("user__${it}__notification")
                pusher.disconnect()
            }
        }
    }

        fun changeFirstLoad() {
            _firstLoad.value = !firstLoad.value
        }

        fun changeUsername(inputUsername: String) {
            if (inputUsername.trim().length <= 15) {
                _newUsername.value = inputUsername.lowercase().trim()
            }
        }

        fun resetSendNewtTweet() {
            _isValidNewTweetForm.value = false
            _newTweetresponse.value = ApiResponse.Idle()
        }

        fun sendNewTweet(token: String, content: String) {
            _newTweetresponse.value = ApiResponse.Loading()
            viewModelScope.launch(Dispatchers.IO) {
                _newTweetresponse.value = repository.sendNewTweet(token, content)
            }
        }

        fun setUsername(token: String) {
            _changeUsernameResponse.value = ApiResponse.Loading()
            viewModelScope.launch {
                val response = repository.setNewUsername(token, newUsername.value)
                if (response is ApiResponse.Success) {
                    _isUsernameSet.value = true
                    authStoreRepository.changeUsername(response.data?.data?.username ?: "")
                }
                _changeUsernameResponse.value = response
            }
        }

        fun loadItems(token: String) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.getLatestTweet(token, _date.value)
                    .cachedIn(viewModelScope)
                    .combine(tweetStatus) { pagingData, status ->
                        val updatedPagingData = pagingData.map { tweet ->
                            if (status.any { it.id == tweet.id }) {
                                // Update the like and tweet status if the id is in the list
                                val updatedLikedStatus = status.first { it.id == tweet.id }.liked
                                val updatedRetweetedStatus =
                                    status.first { it.id == tweet.id }.retweeted
                                tweet.copy(
                                    liked = updatedLikedStatus,
                                    retweteed = updatedRetweetedStatus
                                )
                            } else {
                                tweet
                            }
                        }
                        updatedPagingData
                    }
                    .cachedIn(viewModelScope)
                    .collectLatest {
                        _tweets.value = it
                    }
                _isRefresh.value = false
            }
        }

        fun likeAction(tweet: Tweet) {
            viewModelScope.launch(Dispatchers.IO) {
                val indexTweetInTemporary = tweetStatus.value.indexOfFirst { it.id == tweet.id }
                val currentList = tweetStatus.value.toMutableList()
                if (indexTweetInTemporary != -1) {
                    currentList[indexTweetInTemporary] =
                        TweetStatus(tweet.id, !tweet.liked, tweet.retweteed)
                } else {
                    currentList.add(
                        TweetStatus(
                            id = tweet.id,
                            liked = !tweet.liked,
                            retweeted = tweet.retweteed
                        )
                    )
                }
                withContext(Dispatchers.Main) {
                    tweetStatus.update { currentList }
                }
                token.collectLatest {
                    repository.like(it, tweet.id)
                }
            }
        }

        fun retweetAction(tweet: Tweet) {
            viewModelScope.launch(Dispatchers.IO) {
                val indexTweetInTemporary = tweetStatus.value.indexOfFirst { it.id == tweet.id }
                val currentList: MutableList<TweetStatus> = tweetStatus.value.toMutableList()
                if (indexTweetInTemporary != -1) {
                    currentList[indexTweetInTemporary] =
                        TweetStatus(tweet.id, tweet.liked, !tweet.retweteed)
                } else {
                    currentList.add(
                        TweetStatus(
                            id = tweet.id,
                            liked = tweet.liked,
                            retweeted = !tweet.retweteed
                        )
                    )
                }
                withContext(Dispatchers.Main) {
                    tweetStatus.update {
                        currentList
                    }
                }
                token.collectLatest {
                    repository.retweet(it, tweet.id)
                }
            }
        }
}