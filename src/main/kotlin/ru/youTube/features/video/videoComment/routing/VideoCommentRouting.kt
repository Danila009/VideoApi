package ru.youTube.features.video.videoComment.routing

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import ru.youTube.database.videoComment.dto.CreateVideoCommentDTO
import ru.youTube.database.videoComment.enums.VideoCommentSorting
import ru.youTube.features.video.videoComment.controller.VideoCommentController

fun Routing.configureVideoCommentRouting() {

    val videoCommentController = inject<VideoCommentController>().value

    route("/api/video/comment"){
        get {
            val search = call.request.queryParameters["search"]
            val pageNumber = call.request.queryParameters["pageNumber"]?.toIntOrNull() ?: 1
            val pageSize = call.request.queryParameters["pageSize"]?.toIntOrNull() ?: 20
            val sortingTypeRequest = call.request.queryParameters["sortingType"]

            val sortingType = if (sortingTypeRequest != null)
                enumValueOf<VideoCommentSorting>(sortingTypeRequest)
            else
                null

            val response = videoCommentController.getComments(
                search = search,
                pageNumber = pageNumber,
                pageSize = pageSize,
                sortingType = sortingType
            )
            call.respond(response)
        }

        get("/{id}") {
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