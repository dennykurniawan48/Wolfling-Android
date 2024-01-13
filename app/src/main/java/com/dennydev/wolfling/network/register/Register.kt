package com.dennydev.wolfling.network.register

import com.dennydev.wolfling.model.common.ApiResponse
import com.dennydev.wolfling.model.register.Register

interface Register {
    suspend fun register(name: String, email: String, password: String): ApiResponse<Register>
}