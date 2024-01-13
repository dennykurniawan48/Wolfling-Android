package com.dennydev.wolfling.repository.login

import com.dennydev.wolfling.model.common.ApiResponse
import com.dennydev.wolfling.model.login.Login
import com.dennydev.wolfling.network.login.LoginImpl
import dagger.hilt.android.scopes.ViewModelScoped
import io.ktor.client.HttpClient
import javax.inject.Inject

@ViewModelScoped
class LoginRepository @Inject constructor(
    private val client: HttpClient
) {
    suspend fun login(
        username: String,
        password: String
    ):ApiResponse<Login>{
        return LoginImpl(client).loginCredentials(username, password)
    }

    suspend fun loginGoogle(
        token: String
    ): ApiResponse<Login>{
        return LoginImpl(client).loginGoogle(token)
    }
}