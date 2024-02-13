package newjeans.bunnies.newjeansbunnies.global.security.jwt


import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys

import newjeans.bunnies.newjeansbunnies.global.error.exception.*
import newjeans.bunnies.newjeansbunnies.global.security.principle.CustomManagerDetailsService
import newjeans.bunnies.newjeansbunnies.global.security.principle.CustomUserDetailsService

import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component

import java.nio.charset.StandardCharsets
import java.security.Key


@Component
@Configuration
class JwtParser(
    private val jwtProperties: JwtProperties,
    private val userDetailsService: CustomUserDetailsService,
    private val managerDetailsService: CustomManagerDetailsService
) {

    fun getAuthentication(token: String): Authentication {
        val claims = getClaims(token)

        if (claims.header[Header.JWT_TYPE] != JwtProvider.ACCESS) {
            throw InvalidTokenException
        }
        val userDetails = getDetails(claims.body)

        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    fun getClaims(token: String): Jws<Claims> {
        return try {
            val key: Key = Keys.hmacShaKeyFor(jwtProperties.key.toByteArray(StandardCharsets.UTF_8))
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
        } catch (e: Exception) {
            when (e) {
                is InvalidClaimException -> throw InvalidTokenException
                is ExpiredJwtException -> throw ExpiredTokenException
                is JwtException -> throw UnexpectedTokenException
                else -> throw InternalServerErrorException
            }
        }
    }

    private fun getDetails(body: Claims): UserDetails {
        return when (body["authority"].toString()) {
            "USER" -> userDetailsService.loadUserByUsername(body.id)
            "MANAGER" -> managerDetailsService.loadUserByUsername(body.id)
            else -> throw InvalidRoleException
        }
    }
}