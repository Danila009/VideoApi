package ru.youTube.database.user.dto

@kotlinx.serialization.Serializable
data class UserChannelDTO(
    val id:Int,
    val title:String,
    val description:String
)