package ru.youTube.database.video.dto

@kotlinx.serialization.Serializable
data class CreateVideoDTO(
    val title:String,
    val description:String,
    val previewsUrl:String,
    val videoUrl:String,
    val idChannel:Int
)
