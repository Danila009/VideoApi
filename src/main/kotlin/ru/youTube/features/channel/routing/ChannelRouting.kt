package ru.youTube.features.channel.routing

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
import ru.youTube.common.ConstantsPath
import ru.youTube.common.extensions.save
import ru.youTube.database.channel.dto.CreateChannelDTO
import ru.youTube.database.channel.enums.ChannelSortingType
import ru.youTube.features.channel.controller.ChannelController
import ru.youTube.features.user.controller.UserController
import java.io.File

fun Routing.configureChannelRouting(){

    val channelController = inject<ChannelController>().value
    val userController = inject<UserController>().value

    route("/api/channel"){

        get {
            val search = call.request.queryParameters["search"]
            val pageSize = call.request.queryParameters["pageSize"]?.toIntOrNull() ?: 20
            val pageNumber = call.request.queryParameters["pageNumber"]?.toIntOrNull() ?: 1

            val sortingTypeRequest = call.request.queryParameters["sortingType"]
            val sortingType:ChannelSortingType? = if (sortingTypeRequest != null)
                    enumValueOf<ChannelSortingType>(sortingTypeRequest)
                else
                    null

            val response = channelController.getChannels(
                search = search,
                sortingType = sortingType,
                pageSize = pageSize,
                pageNumber = pageNumber
            )
            call.respond(response)
        }

        get("/{id}") {
            val id = call.parameters["id"]!!.toInt()
            val response = channelController.getChannelById(id)
            call.respond(response)
        }

        get("/icon/{id}") {
            val iconId = call.parameters["id"]!!
            val file = File(ConstantsPath.CHANNEL_ICON_PATH + iconId + ".jpg")

            if (!file.exists())
                return@get call.respond(HttpStatusCode.NotFound)

            call.respondFile(file)
        }

        post("/icon") {
            val multipart = call.receiveMultipart()

            multipart.forEachPart { partData ->
                if (partData is PartData.FileItem){
                    val fileName = partData.save(ConstantsPath.CHANNEL_ICON_PATH)
                    call.respond(
                        "$BASE_URL/api/channel/icon/$fileName"
                    )
                }
            }
        }

        authenticate {
            get("/user") {
                val principal = call.principal<JWTPrincipal>()
                val idUser = principal!!.payload.getClaim("id").asInt()
                val response = channelController.getChannelByUserId(idUser)
                call.respond(response)
            }

            post {
                val principal = call.principal<JWTPrincipal>()
                val id = principal!!.payload.getClaim("id").asInt()
                val channel = call.receive<CreateChannelDTO>()
                channelController.createChannel(channel,id)
                call.respond(HttpStatusCode.OK)
            }

            delete("/{id}") {
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