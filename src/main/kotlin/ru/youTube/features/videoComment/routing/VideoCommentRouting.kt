package ru.youTube.features.videoComment.routing

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import ru.youTube.database.videoComment.dto.CreateVideoCommentDTO
import ru.youTube.features.videoComment.controller.VideoCommentController

fun Routing.configureVideoCommentRouting() {

    val videoCommentController = inject<VideoCommentController>().value

    route("/api/video/comment"){
        get {
            val response = videoCommentController.getComments()
            call.respond(response)
        }

        get("{id}") {
            val id = call.parameters["id"]!!.toInt()
            val response = videoCommentController.getCommentById(id)
            call.respond(response)
        }

        authenticate {
            post {
                val comment = call.receive<CreateVideoCommentDTO>()

                val principal = call.principal<JWTPrincipal>()
                val idUser = principal!!.payload.getClaim("id").asInt()

                val response = videoCommentController.insertComment(comment, idUser)
                call.respond(response)
            }
        }
    }
}