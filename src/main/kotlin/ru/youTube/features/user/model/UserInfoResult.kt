package ru.youTube.features.user.model

import ru.youTube.database.user.dto.UserChannelDTO

@kotlinx.serialization.Serializable
data class UserInfoResult(
    val login:String? = null,
    val photo:String? = null,
    val username:String? = null,
    val channel: List<UserChannelDTO> = emptyList(),
    val error:String? = null
)