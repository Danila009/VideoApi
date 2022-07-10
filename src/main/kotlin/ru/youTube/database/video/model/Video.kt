package ru.youTube.database.video.model

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.youTube.database.channel.model.Channel
import ru.youTube.database.video.Videos

fun Video.mapToVideo() : VideoModel {
    return VideoModel(
        id = this.id.value,
        title = this.title,
        description = this.description,
        previewsUrl = this.previewsUrl,
        videoUrl = this.videoUrl,
        channel = VideoChannelModel(
            id = this.channel.id.value,
            title = this.channel.title,
            description = this.channel.description
        )
    )
}

@kotlinx.serialization.Serializable
data class VideoModel(
    val id:Int,
    val title:String,
    val description:String,
    val previewsUrl:String,
    val videoUrl:String,
    val channel:VideoChannelModel
)

@kotlinx.serialization.Serializable
data class VideoChannelModel(
    val id:Int,
    val title:String,
    val description:String
)

class Video(id: EntityID<Int>): IntEntity(id){
    companion object : IntEntityClass<Video>(Videos)

    var title by Videos.title
    var description by Videos.description
    var previewsUrl by Videos.previewsUrl
    var videoUrl by Videos.videoUrl
    var channel by Channel referencedOn Videos.channel
}