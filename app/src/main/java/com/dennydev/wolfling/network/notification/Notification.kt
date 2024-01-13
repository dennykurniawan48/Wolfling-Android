package com.dennydev.wolfling.network.notification

import com.dennydev.wolfling.model.common.ApiResponse
import com.dennydev.wolfling.model.detailnotification.DetailNotification
import com.dennydev.wolfling.model.notification.NotificationCount

interface Notification {
    suspend fun getNotificationCount(token: String): ApiResponse<NotificationCount>
    suspend fun getNotificationDetail(token: String): ApiResponse<DetailNotification>
}