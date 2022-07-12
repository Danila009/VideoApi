package ru.youTube.features.video.routing

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import ru.youTube.common.Constants.BASE_URL
import ru.youTube.common.ConstantsPath.VIDEO_PREVIEWS_PATH
import ru.youTube.common.ConstantsPath.VIDEO_VIDEO_PATH
import ru.youTube.common.extensions.save
import ru.youTube.database.video.dto.CreateVideoDTO
import ru.youTube.database.video.enums.VideoSortingType
import ru.youTube.database.videoComment.enums.VideoCommentSorting
import ru.youTube.features.user.controller.UserController
import ru.youTube.features.video.controller.VideoController
import ru.youTube.features.video.videoComment.controller.VideoCommentController
import java.io.File

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

        get("/previews/{id}"){
            val iconId = call.parameters["id"]!!
            val file = File("$VIDEO_PREVIEWS_PATH$iconId.jpg")

            if (!file.exists())
                return@get call.respond(HttpStatusCode.NotFound)

            call.respondFile(file)
        }

        post("/previews") {
            val multipart = call.receiveMultipart()

            multipart.forEachPart { partData ->
                if (partData is PartData.FileItem){
                    val fileName = partData.save(VIDEO_PREVIEWS_PATH)
                    call.respond(
                        "$BASE_URL/api/video/previews/$fileName"
                    )
                }
            }
        }

        get("/video/{id}") {
            val videoId = call.parameters["id"]!!
            val file = File("$VIDEO_VIDEO_PATH$videoId.mp4")

            if (!file.exists())
                return@get call.respond(HttpStatusCode.NotFound)

            call.respondFile(file)
        }

        post("/video") {
            val multipart = call.receiveMultipart()

            multipart.forEachPart { partData ->
                if (partData is PartData.FileItem){
                    val fileName = partData.save(VIDEO_VIDEO_PATH)
                    call.respond(
                        "$BASE_URL/api/video/video/$fileName"
                    )
                }
            }
        }

        get {
            val search = call.request.queryParameters["search"]
            val idGenre = call.request.queryParameters["idGenre"]?.toIntOrNull()
            val pageNumber = call.request.queryParameters["pageNumber"]?.toIntOrNull() ?: 1
            val pageSize = call.request.queryParameters["pageSize"]?.toIntOrNull() ?: 30
            val sortingTypeRequest = call.request.queryParameters["sortingType"]

            val sortingType = if(sortingTypeRequest != null)
                enumValueOf<VideoSortingType>(sortingTypeRequest)
            else
                null

            val response = videoController.getVideos(
                search = search,
                idGenre = idGenre,
                pageNumber = pageNumber,
                pageSize = pageSize,
                sortingType = sortingType
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
            val search = call.request.queryParameters["search"]
            val pageNumber = call.request.queryParameters["pageNumber"]?.toIntOrNull() ?: 1
            val pageSize = call.request.queryParameters["pageSize"]?.toIntOrNull() ?: 20
            val sortingTypeRequest = call.request.queryParameters["sortingType"]

            val sortingType = if(sortingTypeRequest != null)
                enumValueOf<VideoCommentSorting>(sortingTypeRequest)
            else
                null

            val response = videoCommentController.getCommentsByVideoId(
                idVideo = idVideo,
                search = search,
                pageNumber = pageNumber,
                pageSize = pageSize,
                sortingType = sortingType
            )
            call.respond(response)
        }
    }
}