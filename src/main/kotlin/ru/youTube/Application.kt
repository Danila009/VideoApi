package ru.youTube

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import ru.youTube.database.DatabaseFactory
import ru.youTube.plugins.*
import ru.youTube.routing.configureMainRouting

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        DatabaseFactory.init()

        installPlugins()
        configureMainRouting()
    }.start(wait = true)
}
