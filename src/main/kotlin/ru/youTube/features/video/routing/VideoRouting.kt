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

fun Routing.configureVideoRouting() {

    val userController = inject<UserController>().value
    val videoController = inject<VideoController>().value

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
            val response = videoController.getVideos()
            call.respond(response)
        }

        get("/{id}") {
            val id = call.parameters["id"]!!.toInt()
            val response = videoController.getVideoById(id)
            call.respond(response)
        }
    }
}