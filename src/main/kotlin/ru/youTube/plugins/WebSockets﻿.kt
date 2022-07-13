package ru.youTube.plugins

import io.ktor.serialization.kotlinx.*
import io.ktor.server.application.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.json.Json
import java.time.Duration

fun Application.configureWebSockets(){

    install(WebSockets){
        contentConverter = KotlinxWebsocketSerializationConverter(Json)
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false

        extensions {
            install(WebSocketDeflateExtension){

            }
        }
    }
}