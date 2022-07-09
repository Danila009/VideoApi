package ru.youTube.database.channel

import org.jetbrains.exposed.dao.id.IntIdTable

object Channels : IntIdTable("channel") {
    val title = varchar("title", 256)
    val description = varchar("description", 528)
}