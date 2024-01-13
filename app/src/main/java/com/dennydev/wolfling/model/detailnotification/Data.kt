package com.dennydev.wolfling.model.detailnotification

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val DestinationUser: DestinationUser,
    val OriginUser: OriginUser,
    val id: String,
    val opened: Boolean,
    val type: String,
    val userFrom: String,
    val userTo: String
)