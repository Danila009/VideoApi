package ru.youTube

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import ru.youTube.database.DatabaseFactory
import ru.youTube.plugins.*
import ru.youTube.routing.configureMainRouting

const val BASE_URL = "https://api.cfif31.ru/youTube"

fun main() {
    embeddedServer(
        Netty,
        environment = applicationEngineEnvironment {

            build {
//                rootPath = BASE_URL
//
//                config = ApplicationConfig(
//                    configPath = BASE_URL
//                )
            }

            module {
                DatabaseFactory.init()

                installPlugins()
                configureMainRouting()
            }

            connector {
                port = 5008//Не - явно не такой
                host = "0.0.0.0"
            }
        }
    ).start(wait = true)
}