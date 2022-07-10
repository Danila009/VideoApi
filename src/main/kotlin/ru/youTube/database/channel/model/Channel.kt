package ru.youTube.database.channel.model

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.youTube.database.channel.Channels
import ru.youTube.database.user.model.User
import ru.youTube.database.video.Videos
import ru.youTube.database.video.model.Video

fun Channel.mapToChannel():ChannelModel {
    return ChannelModel(
        id = this.id.value,
        title = this.title,
        description = this.description,
        videos = this.videos.map {
            ChannelVideoModel(
                id = it.id.value,
                title = it.title,
                description = it.description,
                previewsUrl = it.previewsUrl,
                videoUrl = it.videoUrl
            )
        }
    )
}

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
    val description:String,
    val videos:List<ChannelVideoModel>
)

class Channel(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Channel>(Channels)

    var title by Channels.title
    var description by Channels.description
    val videos  by Video referrersOn Videos.channel
    var user by User referencedOn Channels.user
}