package ru.youTube.features.user.model

@kotlinx.serialization.Serializable
data class AuthorizationResult(
    val token:String? = null,
    val error:String? = null
)