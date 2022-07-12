package ru.youTube.database.video.model

import kotlinx.datetime.LocalDateTime
import ru.youTube.database.video.Video

fun Video.mapToModel() : VideoModel {
    return VideoModel(
        id = this.id.value,
        title = this.title,
        datePublication = this.datePublication,
        description = this.description,
        previewsUrl = this.previewsUrl,
        videoUrl = this.videoUrl,
        channel = VideoChannelModel(
            id = this.channel.id.value,
            title = this.channel.title,
            icon = this.channel.icon,
            description = this.channel.description
        ),
        genre = VideoGenre(
            id = this.genre.id.value,
            name = this.genre.name
        )
    )
}

@kotlinx.serialization.Serializable
data class VideoModel(
    val id:Int,
    val title:String,
    val datePublication: LocalDateTime,
    val description:String,
    val previewsUrl:String,
    val videoUrl:String,
    val channel:VideoChannelModel,
    val genre:VideoGenre
)

@kotlinx.serialization.Serializable
data class VideoChannelModel(
    val id:Int,
    val title:String,
    val icon:String,
    val description:String
)

@kotlinx.serialization.Serializable
data class VideoGenre(
    val id:Int,
    val name:String
)