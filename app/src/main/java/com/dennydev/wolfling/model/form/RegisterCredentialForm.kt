package com.dennydev.wolfling.model.form

import kotlinx.serialization.Serializable

@Serializable
data class RegisterCredentialForm(
    val name: String,
    val email: String,
    val password: String
)