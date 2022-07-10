package ru.youTube.database.videoComment.dto

@kotlinx.serialization.Serializable
data class CreateVideoCommentDTO(
    val description:String,
    val videoId:Int
)
