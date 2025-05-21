package br.com.highbee.highbee_api.security

import br.com.highbee.highbee_api.user.User
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.jackson.io.JacksonDeserializer
import io.jsonwebtoken.jackson.io.JacksonSerializer
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.*

@Component
class Jwt {
    fun createToken(user: User): String =
        UserToken(user).let {
            Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(SECRET.toByteArray())) //CREATE TOKEN
                .json(JacksonSerializer()) //SERIALIZER
                .issuedAt(utcNow().toDate()) //TOKEN WITH TIME NOW
                .expiration(
                    utcNow().plusHours(if (it.isAdmin) ADMIN_EXPIRE_HOURS else EXPIRE_HOURS).toDate()
                ) //Expire Date TOKEN
                .issuer(ISSUER) //! Server name
                .subject(user.id.toString()) //The object how we appoint to discovery who is this guy
                .claim(USER_FIELD, it) //Bonus Information
                .compact() //GENERATE THE TEXT
        }

    fun extract(req: HttpServletRequest): Authentication? {
        try {
            val header = req.getHeader(AUTHORIZATION)
            if(header == null || header.startsWith("bearer ")) return null

            val token = header.replace("Bearer ", "").trim()

            val claims = Jwts
                .parser().verifyWith(Keys.hmacShaKeyFor(SECRET.toByteArray()))
                .json(JacksonDeserializer(mapOf(USER_FIELD to UserToken::class.java)))
                .build()
                .parseSignedClaims(token)
                .payload

            if(claims.issuer != ISSUER) return null

            return claims.get("user", UserToken::class.java).toAuthentication()

        }
        catch (e: Throwable) {
            log.debug("Token Rejected", e)
            return null
        }
    }

    companion object{
        var log = LoggerFactory.getLogger(Jwt::class.java)
        const val SECRET = "HIGHyQ5DeTouW6F9DVpUA6bs7Psz1NxBlShL1sH513EoHImIhFHKZBEE"
        const val EXPIRE_HOURS = 80000L
        const val ADMIN_EXPIRE_HOURS = 120000L
        const val ISSUER = "HighBee"
        const val USER_FIELD = "user"

        private fun utcNow() = ZonedDateTime.now(ZoneOffset.UTC)
        private fun ZonedDateTime.toDate() = Date.from(this.toInstant())
        private fun UserToken.toAuthentication(): Authentication {
            val authorities = roles.map { SimpleGrantedAuthority("ROLE_$it") }
            return UsernamePasswordAuthenticationToken(
                this,
                id,
                authorities
            )
        }

    }
}