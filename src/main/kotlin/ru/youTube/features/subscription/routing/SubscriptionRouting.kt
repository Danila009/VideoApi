package ru.youTube.features.subscription.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import ru.youTube.database.subscription.dto.CreateSubscriptionDTO
import ru.youTube.database.subscription.enums.SubscriptionSortingType
import ru.youTube.features.subscription.controller.SubscriptionController

fun Routing.configureSubscriptionRouting() {

    val subscriptionController = inject<SubscriptionController>().value

    route("/youTube/api/subscription") {

        get("/channel/{id}") {
            val idChannel = call.parameters["id"]!!.toInt()
            val response = subscriptionController.getUserSubscriptionChannel(idChannel)
            call.respond(response)
        }

        get("/channel/{id}/total") {
            val idChannel = call.parameters["id"]!!.toInt()
            val response = subscriptionController.getSubscriptionsChannelTotal(idChannel)
            call.respond(response)
        }

        authenticate {
            get("/user") {
                val principal = call.principal<JWTPrincipal>()
                val id = principal!!.payload.getClaim("id").asInt()

                val search = call.request.queryParameters["search"]
                val pageNumber = call.request.queryParameters["pageNumber"]?.toIntOrNull() ?: 1
                val pageSize = call.request.queryParameters["pageSize"]?.toIntOrNull() ?: 20
                val sortingTypeRequest = call.request.queryParameters["sortingType"]

                val sortingType = if(sortingTypeRequest != null)
                    enumValueOf<SubscriptionSortingType>(sortingTypeRequest)
                else
                    null

                val response = subscriptionController.getSubscriptionsByIdUser(
                    idUser = id,
                    search = search,
                    pageSize = pageSize,
                    pageNumber = pageNumber,
                    sortingType = sortingType
                )

                call.respond(response)
            }

            get("/channel/{id}/check") {
                val principal = call.principal<JWTPrincipal>()
                val idUser = principal!!.payload.getClaim("id").asInt()
                val idChannel = call.parameters["id"]!!.toInt()
                val response = subscriptionController.getCheckSubscriptionChannel(idChannel, idUser)
                call.respond(response)
            }

            get("/user/total") {
                val principal = call.principal<JWTPrincipal>()
                val idUser = principal!!.payload.getClaim("id").asInt()
                val response = subscriptionController.getUserSubscriptionsChannelTotal(idUser)
                call.respond(response)
            }

            post {
                val principal = call.principal<JWTPrincipal>()
                val idUser = principal!!.payload.getClaim("id").asInt()
                val create = call.receive<CreateSubscriptionDTO>()
                subscriptionController.addSubscription(create,idUser)
                call.respond(HttpStatusCode.OK)
            }

            delete("/{id}") {
                val principal = call.principal<JWTPrincipal>()
                val idUser = principal!!.payload.getClaim("id").asInt()
                val id = call.parameters["id"]!!.toInt()
                subscriptionController.deleteSubscription(id, idUser)
                call.respond(HttpStatusCode.OK)
            }

            delete("/channel/{id}") {
                val principal = call.principal<JWTPrincipal>()
                val idUser = principal!!.payload.getClaim("id").asInt()
                val idChannel = call.parameters["id"]!!.toInt()
                subscriptionController.deleteSubscriptionByIdChannel(idChannel, idUser)
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}