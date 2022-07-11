package ru.youTube.database.user.dto

@kotlinx.serialization.Serializable
data class UserChannelDTO(
    val id:Int,
    val title:String,
    val icon:String,
    val description:String
)