package com.dennydev.wolfling.network.login

import com.dennydev.wolfling.model.common.ApiResponse
import com.dennydev.wolfling.model.login.Login as LoginResponse

interface Login {
    suspend fun loginCredentials(username: String, password: String): ApiResponse<LoginResponse>
    suspend fun loginGoogle(token: String): ApiResponse<LoginResponse>
}