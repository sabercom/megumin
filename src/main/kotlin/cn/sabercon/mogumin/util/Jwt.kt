package cn.sabercon.mogumin.util

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.time.ZonedDateTime
import java.util.*

object JwtUtils {
    private val secretKey by lazy { ContextHolder.getProperty("sabercon.jwt-key") }

    private val algorithm by lazy { Algorithm.HMAC256(secretKey) }

    private val verifier by lazy { JWT.require(algorithm).build() }

    fun createToken(subject: String) = JWT.create().withSubject(subject)
        .withExpiresAt(ZonedDateTime.now().plusMonths(1).toInstant().let { Date.from(it) })
        .sign(algorithm)!!

    fun decodeToken(token: String) = wrapExceptionToNull { verifier.verify(token) }
}