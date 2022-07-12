package ru.youTube.features.channel.controller

import ru.youTube.database.channel.dto.CreateChannelDTO
import ru.youTube.database.channel.enums.ChannelSortingType
import ru.youTube.database.channel.model.ChannelModel

interface ChannelController {

    suspend fun getChannels(
        search:String? = null,
        sortingType: ChannelSortingType? = null,
        pageNumber:Int = 1,
        pageSize:Int = 20
    ):List<ChannelModel>

    suspend fun getChannelById(id:Int):ChannelModel

    suspend fun getChannelByUserId(idUser:Int):List<ChannelModel>

    suspend fun createChannel(channel: CreateChannelDTO, userId:Int)

    suspend fun deleteChannel(id:Int)
}