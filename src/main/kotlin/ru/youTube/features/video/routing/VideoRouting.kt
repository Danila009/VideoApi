package ru.youTube.features.video.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import ru.youTube.database.video.dto.CreateVideoDTO
import ru.youTube.features.video.controller.VideoController

fun Routing.configureVideoRouting() {

    val controller = inject<VideoController>().value

    route("/api/video"){
        get {
            val response = controller.getVideos()
            call.respond(response)
        }

        get("/{id}") {
            val id = call.parameters["id"]!!.toInt()
            val response = controller.getVideoById(id)
            if (response != null){
                call.respond(response)
            }else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        post {
            val video = call.receive<CreateVideoDTO>()
            val response = controller.insert(video)
            call.respond(response)
        }

        delete("/{id}") {
            val id = call.parameters["id"]!!.toInt()
            val response = controller.deleteById(id)
            call.respond(response)
        }
    }
}