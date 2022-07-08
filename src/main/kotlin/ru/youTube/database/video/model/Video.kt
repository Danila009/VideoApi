package ru.youTube.database.video.model

import org.jetbrains.exposed.sql.ResultRow
import ru.youTube.database.video.Videos

fun ResultRow.mapToVideo() : Video {
    return Video(
        id = this[Videos.id],
        title = this[Videos.title],
        previewsUrl = this[Videos.previewsUrl],
        videoUrl = this[Videos.videoUrl]
    )
}

@kotlinx.serialization.Serializable
data class Video(
    val id:Int,
    val title:String,
    val previewsUrl:String,
    val videoUrl:String
)