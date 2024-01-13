package com.dennydev.wolfling.repository.register

import com.dennydev.wolfling.model.common.ApiResponse
import com.dennydev.wolfling.model.register.Register
import com.dennydev.wolfling.network.register.RegisterImpl
import dagger.hilt.android.scopes.ViewModelScoped
import io.ktor.client.HttpClient
import javax.inject.Inject

@ViewModelScoped
class RegisterRepository @Inject constructor(
    private val client: HttpClient
) {
    suspend fun register(name: String, email: String, password: String): ApiResponse<Register>{
        return RegisterImpl(client).register(name, email, password)
    }
}