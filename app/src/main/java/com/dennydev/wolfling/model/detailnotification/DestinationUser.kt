package com.dennydev.wolfling.model.detailnotification

import kotlinx.serialization.Serializable

@Serializable
data class DestinationUser(
    val id: String,
    val image: String?=null,
    val name: String,
    val username: String?=null
)