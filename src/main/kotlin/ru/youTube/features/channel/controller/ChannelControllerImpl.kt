package ru.youTube.features.channel.controller

import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.youTube.database.channel.ChannelDAO
import ru.youTube.database.channel.dto.CreateChannelDTO
import ru.youTube.database.channel.model.ChannelModel

class ChannelControllerImpl(
    private val dao:ChannelDAO
): ChannelController {
    override suspend fun getChannels(): List<ChannelModel> = newSuspendedTransaction {
        return@newSuspendedTransaction dao.getChannels()
    }

    override suspend fun getChannelById(id: Int): ChannelModel = newSuspendedTransaction {
        return@newSuspendedTransaction dao.getChannelById(id)
    }

    override suspend fun createChannel(channel: CreateChannelDTO, userId:Int) =
        newSuspendedTransaction {
            return@newSuspendedTransaction dao.createChannel(channel, userId)
        }

    override suspend fun deleteChannel(id: Int) = newSuspendedTransaction {
        dao.deleteChannel(id)
    }
}