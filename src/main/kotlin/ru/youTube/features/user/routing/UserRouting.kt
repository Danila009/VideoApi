package ru.youTube.features.user.routing

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import ru.youTube.BASE_URL
import ru.youTube.common.ConstantsPath
import ru.youTube.common.extensions.save
import ru.youTube.database.user.dto.AuthorizationUserDTO
import ru.youTube.database.user.dto.RegistrationUserDTO
import ru.youTube.features.user.controller.UserController
import java.io.File

fun Routing.configureUserRouting() {

    val controller = inject<UserController>().value

    route("/api/user"){

        authenticate {
            get("/info") {
                val principal = call.principal<JWTPrincipal>()
                val id = principal!!.payload.getClaim("id").asInt()
                val response = controller.getUserInfo(id)
                call.respond(response)
            }


            get("/photo") {
                val principal = call.principal<JWTPrincipal>()
                    ?: return@get call.respond(HttpStatusCode.NotFound)

                val idUserToken = principal.payload.getClaim("id").asInt()
                    ?: return@get call.respond(HttpStatusCode.NotFound)

                val file = File("file/user/photo/$idUserToken.jpg")

                if (!file.exists())
                    return@get call.respond(HttpStatusCode.NotFound)

                call.respondFile(file)
            }

            post("/photo") {
                val multipart = call.receiveMultipart()

                val principal = call.principal<JWTPrincipal>()
                    ?: return@post call.respond(HttpStatusCode.NotFound)

                val idUserToken = principal.payload.getClaim("id").asInt()
                    ?: return@post call.respond(HttpStatusCode.NotFound)

                multipart.forEachPart { partData ->
                    when(partData){
                        is PartData.FormItem -> Unit
                        is PartData.FileItem ->{
                            val fileName = partData.save(ConstantsPath.USER_IMAGES_PATH)
                            controller.updateUserPhoto(
                                id = idUserToken,
                                photoUrl = "$BASE_URL/api/user/photo/$fileName"
                            )
                            call.respond(fileName)
                        }
                        is PartData.BinaryItem -> Unit
                        is PartData.BinaryChannelItem -> Unit
                    }
                }
            }
        }

        get("/photo/{id}") {
            val idUserParameter = call.parameters["id"]!!

            val file = File("file/user/photo/$idUserParameter.jpg")

            if (!file.exists())
                return@get call.respond(HttpStatusCode.NotFound)

            call.respondFile(file)
        }

        post("/authorization"){
            val user = call.receive<AuthorizationUserDTO>()
            val response = controller.authorization(user)
            call.respond(response)
        }

        post("/registration") {
            val registration = call.receive<RegistrationUserDTO>()
            val response = controller.registration(registration)
            call.respond(response)
        }
    }
}