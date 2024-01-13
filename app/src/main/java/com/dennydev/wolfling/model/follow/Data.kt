package com.dennydev.wolfling.model.follow

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val following: Boolean
)