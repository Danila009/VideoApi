package ru.youTube.routing

import io.ktor.server.routing.*
import io.ktor.server.application.*
import ru.youTube.features.channel.routing.configureChannelRouting
import ru.youTube.features.user.routing.configureUserRouting
import ru.youTube.features.video.routing.configureVideoRouting
import ru.youTube.features.videoComment.routing.configureVideoCommentRouting

fun Application.configureRouting() {

    routing {
        configureVideoRouting()
        configureUserRouting()
        configureChannelRouting()
        configureVideoCommentRouting()
    }

}