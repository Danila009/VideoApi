package ru.youTube.database.channel

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.selectAll
import ru.youTube.database.channel.dto.CreateChannelDTO
import ru.youTube.database.channel.model.Channel
import ru.youTube.database.channel.model.ChannelModel
import ru.youTube.database.channel.model.mapToModel
import ru.youTube.database.user.Users
import ru.youTube.database.user.model.User

object Channels : IntIdTable("channel"), ChannelDAO {
    val title = varchar("title", 256)
    val description = varchar("description", 528)
    val icon = varchar("icon", 1080)
    val user = reference("user", Users)

    override fun getChannels(): List<ChannelModel> {
        return selectAll().mapNotNull { Channel[it[id]].mapToModel() }
    }

    override fun getChannelById(id: Int): ChannelModel {
        return Channel[id].mapToModel()
    }

    override fun getChannelByUserId(idUser: Int): List<ChannelModel> {
        return selectAll()
            .filter {
                val channel = Channel[it[id]].mapToModel()
                channel.user.id == idUser
            }
            .map { Channel[it[id]].mapToModel() }
    }

    override fun createChannel(channel: CreateChannelDTO, userId:Int) {
        Channel.new {
            title = channel.title
            icon = channel.icon
            description = channel.description
            user = User[userId]
        }
    }

    override fun deleteChannel(id: Int) { deleteWhere { Channels.id.eq(id) } }
}