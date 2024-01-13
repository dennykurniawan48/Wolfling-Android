package com.dennydev.wolfling.model.form

import kotlinx.serialization.Serializable

@Serializable
data class ReplyTweetForm(
    val content: String,
    val repliedTo: String
)
