package ru.youTube.features.channel.controller

import ru.youTube.database.channel.dto.CreateChannelDTO
import ru.youTube.database.channel.model.ChannelModel

interface ChannelController {

    suspend fun getChannels():List<ChannelModel>

    suspend fun getChannelById(id:Int):ChannelModel

    suspend fun getChannelByUserId(idUser:Int):List<ChannelModel>

    suspend fun createChannel(channel: CreateChannelDTO, userId:Int)

    suspend fun deleteChannel(id:Int)
}