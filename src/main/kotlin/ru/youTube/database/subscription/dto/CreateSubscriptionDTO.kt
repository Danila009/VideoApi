package ru.youTube.database.subscription.dto

@kotlinx.serialization.Serializable
data class CreateSubscriptionDTO(
    val channelId:Int
)
