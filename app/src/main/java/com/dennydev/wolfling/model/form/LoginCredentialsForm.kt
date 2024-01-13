package com.dennydev.wolfling.model.form

import kotlinx.serialization.Serializable

@Serializable
data class LoginCredentialsForm(
    val username: String,
    val password: String
)
