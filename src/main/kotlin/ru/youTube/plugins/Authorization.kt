package ru.youTube.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import ru.youTube.auth.AuthConfig
import ru.youTube.auth.common.ConstantsAuth.AUTH_AUDIENCE
import ru.youTube.auth.common.ConstantsAuth.AUTH_ISSUER
import ru.youTube.auth.common.ConstantsAuth.AUTH_MY_REALM
import ru.youTube.auth.common.ConstantsAuth.AUTH_SECRET

fun Application.configureAuthentication(){

    val config = AuthConfig(
        secret = AUTH_SECRET,
        issuer = AUTH_ISSUER,
        audience = AUTH_AUDIENCE,
        myRealm = AUTH_MY_REALM
    )

    install(Authentication){
        jwt {
            realm = config.myRealm

            verifier(
                JWT
                .require(Algorithm.HMAC256(config.secret))
                .withAudience(config.audience)
                .withIssuer(config.issuer)
                .build())

            validate {jwtCredential ->
                if (jwtCredential.payload.getClaim("id") != null)
                    JWTPrincipal(jwtCredential.payload)
                else
                    null
            }

            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }
}