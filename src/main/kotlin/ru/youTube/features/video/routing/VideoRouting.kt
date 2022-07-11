package ru.youTube.features.video.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import ru.youTube.database.video.dto.CreateVideoDTO
import ru.youTube.features.user.controller.UserController
import ru.youTube.features.video.controller.VideoController
import ru.youTube.features.video.videoComment.controller.VideoCommentController

fun Routing.configureVideoRouting() {

    val userController = inject<UserController>().value
    val videoController = inject<VideoController>().value
    val videoCommentController = inject<VideoCommentController>().value

    route("/api/video"){
        authenticate {
            post {
                val video = call.receive<CreateVideoDTO>()

                val principal = call.principal<JWTPrincipal>()
                val idUser = principal!!.payload.getClaim("id").asInt()

                val user = userController.getUserInfo(idUser)

                if (!user.channel.any { it.id == video.idChannel })
                    return@post call.respond(HttpStatusCode.NotFound)

                val response = videoController.insert(video)
                call.respond(response)
            }

            delete("/{id}") {
                val id = call.parameters["id"]!!.toInt()

                val principal = call.principal<JWTPrincipal>()
                val idUser = principal!!.payload.getClaim("id").asInt()

                val user = userController.getUserInfo(idUser)

                if (!user.channel.any { it.id == id })
                    return@delete call.respond(HttpStatusCode.NotFound)

                val response = videoController.deleteById(id)
                call.respond(response)
            }
        }

        get {
            val search = call.request.queryParameters["search"]
            val idGenre = call.request.queryParameters["idGenre"]?.toIntOrNull()
            val response = videoController.getVideos(
                search = search, idGenre
            )
            call.respond(response)
        }

        get("/{id}") {
            val id = call.parameters["id"]!!.toInt()
            val response = videoController.getVideoById(id)
            call.respond(response)
        }

        get("/{id}/comments") {
            val idVideo = call.parameters["id"]!!.toInt()
            val response = videoCommentController.getCommentsByVideoId(idVideo)
            call.respond(response)
        }
    }
}