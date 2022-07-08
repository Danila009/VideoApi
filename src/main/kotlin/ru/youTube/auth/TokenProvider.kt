package ru.youTube.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

class TokenProvider(
    private val authConfig: AuthConfig
) {

    fun createToken(userId: Int): String {
        return JWT.create()
            .withAudience(authConfig.audience)
            .withIssuer(authConfig.issuer)
            .withClaim("id", userId)
            .withExpiresAt(Date(System.currentTimeMillis() + 3600000L))
            .sign(Algorithm.HMAC256(authConfig.secret))
    }
}