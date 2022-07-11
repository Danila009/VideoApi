package ru.youTube.database.channel

import ru.youTube.database.channel.dto.CreateChannelDTO
import ru.youTube.database.channel.model.ChannelModel

interface ChannelDAO {

    fun getChannels():List<ChannelModel>

    fun getChannelById(id:Int):ChannelModel

    fun getChannelByUserId(idUser:Int):List<ChannelModel>

    fun createChannel(channel:CreateChannelDTO, userId:Int)

    fun deleteChannel(id:Int)
}