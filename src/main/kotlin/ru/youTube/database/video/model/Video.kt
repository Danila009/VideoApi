package ru.youTube.database.video.model

import org.jetbrains.exposed.sql.ResultRow
import ru.youTube.database.channel.Channels
import ru.youTube.database.channel.model.Channel
import ru.youTube.database.user.Users.references
import ru.youTube.database.video.Videos

fun ResultRow.mapToVideo() : Video {
    return Video(
        title = this[Videos.title],
        previewsUrl = this[Videos.previewsUrl],
        videoUrl = this[Videos.videoUrl],
//        channel = Channel(
//            title = this[Videos.channel.references(Channels)],
//            description = this[Videos.channel.ddl.get(Channels.description)]
//        )
    )
}

@kotlinx.serialization.Serializable
data class Video(
    val title:String,
    val previewsUrl:String,
    val videoUrl:String,
//    val channel:Channel
)