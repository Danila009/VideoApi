package ru.youTube.di

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.youTube.auth.AuthConfig
import ru.youTube.auth.TokenProvider
import ru.youTube.auth.common.ConstantsAuth.AUTH_AUDIENCE
import ru.youTube.auth.common.ConstantsAuth.AUTH_ISSUER
import ru.youTube.auth.common.ConstantsAuth.AUTH_MY_REALM
import ru.youTube.auth.common.ConstantsAuth.AUTH_SECRET
import ru.youTube.database.user.UserDAO
import ru.youTube.database.user.Users
import ru.youTube.database.video.VideoDAO
import ru.youTube.database.video.Videos
import ru.youTube.features.user.controller.UserController
import ru.youTube.features.video.controller.VideoController
import ru.youTube.features.video.controller.VideoControllerImpl
import ru.youTube.features.user.controller.UserControllerImpl

val daoModule = module {
    single<VideoDAO> { Videos }
    single<UserDAO> { Users }
}

val controllerModule = module {
    singleOf(::VideoControllerImpl) { bind<VideoController>() }
    singleOf(::UserControllerImpl) { bind<UserController>() }
}

val authModule = module {
    single {
        AuthConfig(
            secret = AUTH_SECRET,
            issuer = AUTH_ISSUER,
            audience = AUTH_AUDIENCE,
            myRealm = AUTH_MY_REALM
        )
    }

    singleOf(::TokenProvider)
}