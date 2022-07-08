package ru.youTube.features.user.model

@kotlinx.serialization.Serializable
data class UserInfoResult(
    val login:String? = null,
    val username:String? = null,
    val error:String? = null
)