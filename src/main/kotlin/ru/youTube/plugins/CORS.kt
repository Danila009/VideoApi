package ru.youTube.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*

fun Application.configureCORS(){
    install(CORS){
        allowHost("0.0.0.0:8080")

        anyHost()

        allowCredentials = true

        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)

        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Patch)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Head)
    }
}