package ru.youTube.database.user.dto

@kotlinx.serialization.Serializable
data class RegistrationUserDTO(
    val username:String,
    val photo:String? = null,
    val login:String,
    val password:String
)