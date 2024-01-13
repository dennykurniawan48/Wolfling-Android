package com.dennydev.wolfling.network.home.setusername

import android.util.Log
import com.dennydev.wolfling.model.common.ApiResponse
import com.dennydev.wolfling.model.common.Constant
import com.dennydev.wolfling.model.profile.Profile
import com.dennydev.wolfling.model.register.Register
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess

class SetUsernameImpl(val client: HttpClient): SetUsername {
    override suspend fun setUsername(token: String,newUsername: String): ApiResponse<Register> {
        return try {
            val response: HttpResponse = client.get(
                Constant.SET_USERNAME_URL.replace("{username}", newUsername)
            ){
                headers{
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            }
            if (response.status.isSuccess()) {
                val myResponse = response.body<Register>()
                ApiResponse.Success(myResponse)
            } else if (response.status.value == 409) {
                ApiResponse.Error("Username already taken.")
            } else {
                ApiResponse.Error("Something went wrong.")
            }
        }catch(e: Exception){
            Log.d("set username error", e.toString())
            ApiResponse.Error("Something went wrong.")
        }
    }
}