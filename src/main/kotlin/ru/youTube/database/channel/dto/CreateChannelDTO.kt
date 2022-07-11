package ru.youTube.database.channel.dto

@kotlinx.serialization.Serializable
data class CreateChannelDTO(
    val title:String,
    val icon:String,
    val description:String
)
