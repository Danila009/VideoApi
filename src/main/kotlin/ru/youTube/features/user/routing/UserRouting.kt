package ru.youTube.features.user.routing

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import ru.youTube.database.user.dto.AuthorizationUserDTO
import ru.youTube.features.user.controller.UserController

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
        }

        post("/authorization"){
            val user = call.receive<AuthorizationUserDTO>()
            val response = controller.authorization(user)
            call.respond(response)
        }
    }
}