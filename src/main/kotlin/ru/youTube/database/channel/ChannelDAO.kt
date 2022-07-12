package ru.youTube.database.channel

import ru.youTube.database.channel.dto.CreateChannelDTO
import ru.youTube.database.channel.enums.ChannelSortingType
import ru.youTube.database.channel.model.ChannelModel

interface ChannelDAO {

    fun getChannels(
        search:String?,sortingType: ChannelSortingType?, pageNumber:Int, pageSize:Int
    ):List<ChannelModel>

    fun getChannelById(id:Int):ChannelModel

    fun getChannelByUserId(idUser:Int):List<ChannelModel>

    fun createChannel(channel:CreateChannelDTO, userId:Int)

    fun deleteChannel(id:Int)
}