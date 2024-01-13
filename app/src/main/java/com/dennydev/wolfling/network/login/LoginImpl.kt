package com.dennydev.wolfling.network.login

import android.util.Log
import com.dennydev.wolfling.model.common.ApiResponse
import com.dennydev.wolfling.model.common.Constant
import com.dennydev.wolfling.model.form.LoginCredentialsForm
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import com.dennydev.wolfling.model.login.Login as LoginResponse

class LoginImpl(val client: HttpClient): Login {
    override suspend fun loginCredentials(username: String, password: String): ApiResponse<LoginResponse> {
        return try {
            val response: HttpResponse = client.post(Constant.LOGIN_CREDENTIALS_URL) {
                contentType(ContentType.Application.Json)
                setBody(LoginCredentialsForm(username, password))
            }
            if (response.status.isSuccess()) {
                val myResponse = response.body<LoginResponse>()
                ApiResponse.Success(myResponse)
            } else if (response.status.value == 401) {
                ApiResponse.Error("Wrong username or password.")
            } else if (response.status.value == 404) {
                ApiResponse.Error("Email isn't registered.")
            } else {
                ApiResponse.Error("Something went wrong.")
            }
        }catch(e: Exception){
            Log.d("error login", e.toString())
            ApiResponse.Error("Something went wrong.")
        }
    }

    override suspend fun loginGoogle(token: String): ApiResponse<LoginResponse> {
        return try {
            val response: HttpResponse = client.get(
                Constant.LOGIN_GOOGLE_URL
                    .replace("{token}", token)
            )
            if (response.status.isSuccess()) {
                val myResponse = response.body<LoginResponse>()
                ApiResponse.Success(myResponse)
            } else if (response.status.value == 401) {
                ApiResponse.Error("Wrong email or password.")
            } else {
                ApiResponse.Error("Something went wrong.")
            }
        }catch(e: Exception){
            ApiResponse.Error("Something went wrong.")
        }
    }

}