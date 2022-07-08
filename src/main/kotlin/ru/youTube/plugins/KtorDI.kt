package ru.youTube.plugins

import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import ru.youTube.di.authModule
import ru.youTube.di.controllerModule
import ru.youTube.di.daoModule

fun Application.configureKtorDI() {
    
    install(Koin){
        modules(
            daoModule,
            controllerModule,
            authModule
        )
    }
}