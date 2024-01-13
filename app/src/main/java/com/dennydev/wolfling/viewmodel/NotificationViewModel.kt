package com.dennydev.wolfling.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dennydev.wolfling.model.common.ApiResponse
import com.dennydev.wolfling.model.detailnotification.DetailNotification
import com.dennydev.wolfling.repository.AuthStoreRepository
import com.dennydev.wolfling.repository.home.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val repository: HomeRepository,
    private val authStoreRepository: AuthStoreRepository
): ViewModel() {
    val token = authStoreRepository.flowToken
    private val _notificationResponse = mutableStateOf<ApiResponse<DetailNotification>>(ApiResponse.Idle())
    val notificationResponse: State<ApiResponse<DetailNotification>> = _notificationResponse


    fun getDetailNotification(token: String) {
        _notificationResponse.value = ApiResponse.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            _notificationResponse.value = repository.getNotificationDetail(token)
        }
    }
}