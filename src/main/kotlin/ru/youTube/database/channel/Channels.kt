package ru.youTube.database.channel

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import org.jetbrains.exposed.sql.selectAll
import ru.youTube.database.channel.dto.CreateChannelDTO
import ru.youTube.database.channel.enums.ChannelSortingType
import ru.youTube.database.channel.model.ChannelModel
import ru.youTube.database.channel.model.mapToModel
import ru.youTube.database.user.User
import ru.youTube.database.user.Users
import ru.youTube.database.video.Videos
import ru.youTube.database.video.Video

class Channel(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Channel>(Channels)

    var title by Channels.title
    var description by Channels.description
    var icon by Channels.icon
    val videos  by Video referrersOn Videos.channel
    var user by User referencedOn Channels.user
    var datePublication by Channels.datePublication
}

object Channels : IntIdTable("channel"), ChannelDAO {
    val title = varchar("title", 256)
    val description = varchar("description", 528)
    val icon = varchar("icon", 1080)
    val user = reference("user", Users)
    val datePublication = datetime("date_publication").defaultExpression(CurrentDateTime())

    override fun getChannels(
        search:String?,
        sortingType: ChannelSortingType?,
        pageNumber:Int,
        pageSize:Int
    ): List<ChannelModel> {
        val channels = selectAll()

        when(sortingType){
            ChannelSortingType.TITLE_ASC -> channels.orderBy(
                title,SortOrder.ASC
            )
            ChannelSortingType.TITLE_DESC -> channels.orderBy(
                title,SortOrder.DESC
            )
            ChannelSortingType.DESCRIPTION_DESC -> channels.orderBy(
                description,SortOrder.DESC
            )
            ChannelSortingType.DESCRIPTION_ASC -> channels.orderBy(
                description,SortOrder.ASC
            )
            ChannelSortingType.DATE_PUBLICATION_ASC -> channels.orderBy(
                datePublication,SortOrder.ASC
            )
            ChannelSortingType.DATE_PUBLICATION_DESC -> channels.orderBy(
                datePublication,SortOrder.DESC
            )
            null -> Unit
        }

        return channels
            .limit(pageSize)
            .drop((pageNumber - 1) * pageSize)
            .filter {
                val channelsModelFilter = Channel[it[id]].mapToModel()

                search?.let {
                    return@filter channelsModelFilter.title.lowercase().contains(search.lowercase())
                            || channelsModelFilter.description.lowercase().contains(search.lowercase())
                }

                true
            }.map { Channel[it[id]].mapToModel() }
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