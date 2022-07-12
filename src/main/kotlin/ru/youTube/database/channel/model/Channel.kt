package ru.youTube.database.channel.model

import kotlinx.datetime.LocalDateTime
import ru.youTube.database.channel.Channel


fun Channel.mapToModel():ChannelModel {
    return ChannelModel(
        id = this.id.value,
        title = this.title,
        icon = this.icon,
        description = this.description,
        datePublication = this.datePublication,
        user = ChannelUser(
            id = this.user.id.value,
            username = this.user.username,
            login = this.user.login,
            photo = this.user.photo
        ),
        videos = this.videos.map {
            ChannelVideoModel(
                id = it.id.value,
                title = it.title,
                description = it.description,
                previewsUrl = it.previewsUrl,
                videoUrl = it.videoUrl,

            )
        }
    )
}

@kotlinx.serialization.Serializable
data class ChannelUser(
    val id:Int,
    val username:String,
    val login:String,
    val photo:String?
)

@kotlinx.serialization.Serializable
data class ChannelVideoModel(
    val id:Int,
    val title:String,
    val description:String,
    val previewsUrl:String,
    val videoUrl:String
)

@kotlinx.serialization.Serializable
data class ChannelModel(
    val id:Int,
    val title:String,
    val icon:String,
    val description:String,
    val datePublication: LocalDateTime,
    val user:ChannelUser,
    val videos:List<ChannelVideoModel>
)