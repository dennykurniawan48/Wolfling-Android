package com.dennydev.wolfling.model.tweetaction.like

import kotlinx.serialization.Serializable

@Serializable
data class LikeAction(
    val `data`: Data
)