package com.dennydev.wolfling.model.form

import kotlinx.serialization.Serializable

@Serializable
data class NewTweetForm(
    val content: String
)