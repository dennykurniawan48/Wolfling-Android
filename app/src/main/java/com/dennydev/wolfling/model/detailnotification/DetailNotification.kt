package com.dennydev.wolfling.model.detailnotification

import kotlinx.serialization.Serializable

@Serializable
data class DetailNotification(
    val `data`: List<Data>
)