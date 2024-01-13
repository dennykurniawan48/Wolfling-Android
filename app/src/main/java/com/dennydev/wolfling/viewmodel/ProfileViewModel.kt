package com.dennydev.wolfling.viewmodel

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
import com.dennydev.wolfling.model.common.TweetStatus
import com.dennydev.wolfling.model.listtweet.Tweet
import com.dennydev.wolfling.model.profile.Profile
import com.dennydev.wolfling.repository.AuthStoreRepository
import com.dennydev.wolfling.repository.home.HomeRepository
import com.dennydev.wolfling.repository.profile.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val authStoreRepository: AuthStoreRepository
): ViewModel() {
    val token = authStoreRepository.flowToken
    val username = authStoreRepository.flowUsername
    private val _following = mutableStateOf(false)
    val following: State<Boolean> = _following
    private val _date: MutableState<String?> = mutableStateOf(null)
    private val tweetStatus = MutableStateFlow(emptyList<TweetStatus>())
    private val _tweets = MutableStateFlow<PagingData<Tweet>>(PagingData.empty(sourceLoadStates = LoadStates(refresh = LoadState.NotLoading(endOfPaginationReached = false), prepend = LoadState.NotLoading(endOfPaginationReached = false), append = LoadState.NotLoading(endOfPaginationReached = false))))
    val tweets: StateFlow<PagingData<Tweet>> = _tweets
    val isGoogle = authStoreRepository.flowGoogle

    private val _profileData = mutableStateOf<ApiResponse<Profile>>(ApiResponse.Idle())
    val profileData: State<ApiResponse<Profile>> = _profileData

    fun getProfile(token: String, username: String){
        _profileData.value = ApiResponse.Loading()
        viewModelScope.launch {
            val result = repository.getProfileData(token, username)
            result.data?.data?.let{
                _following.value = it.following
            }
            _profileData.value = result
        }
    }

    fun loadItems(token: String, username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getProfileTweet(token = token, username, _date.value)
                .cachedIn(viewModelScope)
                .combine(tweetStatus){ pagingData, status ->
                    val updatedPagingData = pagingData.map { tweet ->
                        if (status.any { it.id == tweet.id }) {
                            // Update the like and tweet status if the id is in the list
                            val updatedLikedStatus = status.first { it.id == tweet.id }.liked
                            val updatedRetweetedStatus = status.first { it.id == tweet.id }.retweeted
                            tweet.copy(liked = updatedLikedStatus, retweteed = updatedRetweetedStatus)
                        } else {
                            tweet
                        }
                    }
                    updatedPagingData
                }
                .collectLatest {
                    _tweets.value = it
                }
        }
    }

    fun likeAction(tweet: Tweet){
        viewModelScope.launch(Dispatchers.IO) {
            val indexTweetInTemporary = tweetStatus.value.indexOfFirst { it.id == tweet.id }
            val currentList = tweetStatus.value.toMutableList()
            if (indexTweetInTemporary != -1) {
                currentList[indexTweetInTemporary].liked = !tweet.liked
                currentList[indexTweetInTemporary].retweeted = tweet.retweteed
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
                tweetStatus.value = currentList
            }
        }
    }

    fun retweetAction(tweet: Tweet){
        viewModelScope.launch(Dispatchers.IO) {
            val indexTweetInTemporary = tweetStatus.value.indexOfFirst { it.id == tweet.id }
            val currentList = tweetStatus.value.toMutableList()
            if (indexTweetInTemporary != -1) {
                currentList[indexTweetInTemporary].liked = tweet.liked
                currentList[indexTweetInTemporary].retweeted = !tweet.retweteed
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
                tweetStatus.value = currentList
            }
        }
    }

    fun follow(token: String, userId: String){
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.follow(token, userId)
           result.data?.data?.following?.let {
               _following.value = it
           }
        }
    }


}