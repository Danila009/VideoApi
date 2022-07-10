package ru.youTube.database.videoComment.model

import ru.youTube.database.videoComment.VideoComment

fun VideoComment.mapToModel(): VideoCommentModel {
    return VideoCommentModel(
        id = this.id.value,
        description = this.description,
        user = VideoCommentUser(
            id = this.user.id.value,
            username = this.user.username,
            login = this.user.login,
            photo = this.user.photo
        ),
        video = VideoCommentVideo(
            id = this.video.id.value,
            title = this.video.title,
            description = this.video.description,
            previewsUrl = this.video.previewsUrl,
            videoUrl = this.video.videoUrl,
            channel = VideoCommentVideoChannel(
                id = this.video.channel.id.value,
                title = this.video.channel.title,
                description = this.video.channel.description
            )
        )
    )
}

@kotlinx.serialization.Serializable
data class VideoCommentModel(
    val id:Int,
    val description:String,
    val user:VideoCommentUser,
    val video:VideoCommentVideo
)

@kotlinx.serialization.Serializable
data class VideoCommentVideo(
    val id:Int,
    val title:String,
    val description: String,
    val previewsUrl:String,
    val videoUrl:String,
    val channel:VideoCommentVideoChannel
)

@kotlinx.serialization.Serializable
data class VideoCommentVideoChannel(
    val id:Int,
    val title:String,
    val description: String
)

@kotlinx.serialization.Serializable
data class VideoCommentUser(
    val id:Int,
    val username:String,
    val login:String,
    val photo:String? = null
)