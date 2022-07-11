package ru.youTube.routing

import io.ktor.server.routing.*
import io.ktor.server.application.*
import ru.youTube.features.channel.routing.configureChannelRouting
import ru.youTube.features.subscription.routing.configureSubscriptionRouting
import ru.youTube.features.user.routing.configureUserRouting
import ru.youTube.features.video.routing.configureVideoRouting
import ru.youTube.features.video.videoComment.routing.configureVideoCommentRouting
import ru.youTube.features.video.videoGenre.routing.configureVideoGenreRouting

fun Application.configureMainRouting() {

    routing {
        configureVideoRouting()
        configureUserRouting()
        configureChannelRouting()
        configureVideoCommentRouting()
        configureVideoGenreRouting()
        configureSubscriptionRouting()
    }
}