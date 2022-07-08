package ru.youTube.database.user.dto

@kotlinx.serialization.Serializable
data class AuthorizationUserDTO(
    val login:String,
    val password:String
)
