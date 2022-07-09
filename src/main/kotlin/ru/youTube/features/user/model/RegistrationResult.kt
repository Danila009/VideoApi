package ru.youTube.features.user.model

@kotlinx.serialization.Serializable
data class RegistrationResult(
    val token:String? = null,
    val error:String? = null
)
