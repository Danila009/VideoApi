package ru.youTube.auth

data class AuthConfig(
    val secret:String,
    val issuer:String,
    val audience:String,
    val myRealm:String
)