package ru.youTube.database.user.dto

@kotlinx.serialization.Serializable
data class RegistrationUserDTO(
    val username:String,
    val login:String,
    val password:String
)