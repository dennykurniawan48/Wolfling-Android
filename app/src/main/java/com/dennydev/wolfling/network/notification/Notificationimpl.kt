package com.dennydev.wolfling.network.notification

import android.util.Log
import com.dennydev.wolfling.model.common.ApiResponse
import com.dennydev.wolfling.model.common.Constant
import com.dennydev.wolfling.model.detailnotification.DetailNotification
import com.dennydev.wolfling.model.notification.NotificationCount
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders

class Notificationimpl(val client: HttpClient): Notification {
    override suspend fun getNotificationCount(token: String): ApiResponse<NotificationCount> {
        return try{
            val response = client.get(Constant.NOTIFICATION_URL){
                headers{
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            }
            ApiResponse.Success(response.body())
        }catch(e: Exception){
            Log.d("error get notification", e.toString())
            ApiResponse.Error("Something wen't wrong")
        }
    }

    override suspend fun getNotificationDetail(token: String): ApiResponse<DetailNotification> {
        return try{
            val response = client.get(Constant.NOTIFICATION_DETAIL_URL){
                headers{
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            }
            ApiResponse.Success(response.body())
        }catch(e: Exception){
            Log.d("error clear notification", e.toString())
            ApiResponse.Error("Something wen't wrong")
        }
    }
}