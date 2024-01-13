package com.dennydev.wolfling.network.register

import android.util.Log
import com.dennydev.wolfling.model.common.ApiResponse
import com.dennydev.wolfling.model.common.Constant
import com.dennydev.wolfling.model.form.RegisterCredentialForm
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess


class RegisterImpl(val client: HttpClient): Register {
    override suspend fun register(
        name: String,
        email: String,
        password: String
    ): ApiResponse<com.dennydev.wolfling.model.register.Register> {
        return try {
            val response: HttpResponse = client.post(Constant.REGISTER_CREDENTIALS_URL) {
                contentType(ContentType.Application.Json)
                setBody(
                    RegisterCredentialForm(
                        email = email,
                        name = name,
                        password = password
                    )
                )
            }

            if (response.status.isSuccess()) {
                val myResponse = response.body<com.dennydev.wolfling.model.register.Register>()
                ApiResponse.Success(myResponse)
            } else if (response.status.value == 409) {
                ApiResponse.Error("Email already registered.")
            } else {
                ApiResponse.Error("Something went wrong.")
            }
        }catch (e: Exception){
            Log.d("Error register", e.toString())
            ApiResponse.Error("Something went wrong.")
        }
    }
}