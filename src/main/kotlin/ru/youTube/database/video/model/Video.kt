package ru.youTube.database.video.model

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.youTube.database.channel.model.Channel
import ru.youTube.database.genre.Genre
import ru.youTube.database.video.Videos

fun Video.mapToModel() : VideoModel {
    return VideoModel(
        id = this.id.value,
        title = this.title,
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

class Video(id: EntityID<Int>): IntEntity(id){
    companion object : IntEntityClass<Video>(Videos)

    var title by Videos.title
    var description by Videos.description
    var previewsUrl by Videos.previewsUrl
    var videoUrl by Videos.videoUrl
    var channel by Channel referencedOn Videos.channel
    var genre by Genre referencedOn Videos.genre
}