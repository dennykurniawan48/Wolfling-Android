package com.dennydev.wolfling.network.home.setusername

import com.dennydev.wolfling.model.common.ApiResponse
import com.dennydev.wolfling.model.register.Register

interface SetUsername {
    suspend fun setUsername(token: String, newUsername: String): ApiResponse<Register>
}