package ru.youTube.plugins

import io.ktor.server.application.*

fun Application.installPlugins(){
    configureAuthentication()
    configureKtorDI()
    configureSerialization()
}