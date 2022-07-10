package ru.youTube.features.channel.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import ru.youTube.database.channel.dto.CreateChannelDTO
import ru.youTube.features.channel.controller.ChannelController
import ru.youTube.features.user.controller.UserController

fun Routing.configureChannelRouting(){

    val channelController = inject<ChannelController>().value
    val userController = inject<UserController>().value

    route("/api/channel"){

        get {
            val response = channelController.getChannels()
            call.respond(response)
        }

        get("{id}") {
            val id = call.parameters["id"]!!.toInt()
            val response = channelController.getChannelById(id)
            call.respond(response)
        }

        authenticate {
            post {
                val principal = call.principal<JWTPrincipal>()
                val id = principal!!.payload.getClaim("id").asInt()
                val channel = call.receive<CreateChannelDTO>()
                channelController.createChannel(channel,id)
                call.respond(HttpStatusCode.OK)
            }

            delete("{id}") {
                val principal = call.principal<JWTPrincipal>()
                val idUser = principal!!.payload.getClaim("id").asInt()

                val idChannel = call.parameters["id"]!!.toInt()

                val user = userController.getUserInfo(idUser)

                if (!user.channel.any { it.id == idChannel })
                    return@delete call.respond(HttpStatusCode.NotFound)

                channelController.deleteChannel(idChannel)
                call.respond(HttpStatusCode.OK)
            }
        }
    }

}