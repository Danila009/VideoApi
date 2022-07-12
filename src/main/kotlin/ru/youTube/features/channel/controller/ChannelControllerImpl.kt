package ru.youTube.features.channel.controller

import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.youTube.database.channel.ChannelDAO
import ru.youTube.database.channel.dto.CreateChannelDTO
import ru.youTube.database.channel.enums.ChannelSortingType
import ru.youTube.database.channel.model.ChannelModel

class ChannelControllerImpl(
    private val dao:ChannelDAO
): ChannelController {
    override suspend fun getChannels(
        search:String?,
        sortingType: ChannelSortingType?,
        pageNumber:Int,
        pageSize:Int
    ): List<ChannelModel> = newSuspendedTransaction {
        return@newSuspendedTransaction dao.getChannels(
            search, sortingType, pageNumber, pageSize
        )
    }

    override suspend fun getChannelById(id: Int): ChannelModel = newSuspendedTransaction {
        return@newSuspendedTransaction dao.getChannelById(id)
    }

    override suspend fun getChannelByUserId(idUser: Int): List<ChannelModel> = newSuspendedTransaction {
        return@newSuspendedTransaction dao.getChannelByUserId(idUser)
    }

    override suspend fun createChannel(channel: CreateChannelDTO, userId:Int) =
        newSuspendedTransaction {
            return@newSuspendedTransaction dao.createChannel(channel, userId)
        }

    override suspend fun deleteChannel(id: Int) = newSuspendedTransaction {
        dao.deleteChannel(id)
    }
}