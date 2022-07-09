package ru.youTube.database.channel.model

import org.jetbrains.exposed.sql.ResultRow
import ru.youTube.database.channel.Channels

@kotlinx.serialization.Serializable
data class Channel(
    val title:String,
    val description:String
)

fun ResultRow.mapToChannel() : Channel {
    return Channel(
        title = this[Channels.title],
        description = this[Channels.description]
    )
}
