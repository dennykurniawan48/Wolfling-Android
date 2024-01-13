package com.dennydev.wolfling.model.tweetaction.reply

import kotlinx.serialization.Serializable

@Serializable
data class ReplyAction(
    val `data`: Data
)