package newjeans.bunnies.newjeansbunnies.domain.auth.service

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys

import newjeans.bunnies.newjeansbunnies.domain.auth.RefreshTokenEntity
import newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.TokenDto
import newjeans.bunnies.newjeansbunnies.domain.auth.repository.RefreshTokenRepository
import newjeans.bunnies.newjeansbunnies.global.error.exception.InternalServerErrorException
import newjeans.bunnies.newjeansbunnies.global.error.exception.InvalidTokenException
import newjeans.bunnies.newjeansbunnies.global.error.exception.UnexpectedTokenException
import newjeans.bunnies.newjeansbunnies.global.security.jwt.JwtParser
import newjeans.bunnies.newjeansbunnies.global.security.jwt.JwtProperties
import newjeans.bunnies.newjeansbunnies.global.security.jwt.JwtProvider

import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service

import java.nio.charset.StandardCharsets
import java.security.Key

@Service
@Configuration
class ReissueTokenService(
    private val jwtProperties: JwtProperties,
    private val jwtProvider: JwtProvider,
    private val jwtParser: JwtParser,
    private val refreshTokenRepository: RefreshTokenRepository,
) {
     fun execute(refreshToken: String, accessToken: String): TokenDto {

        val refreshTokenClaims = jwtParser.getClaims(refreshToken)
        checkValidAccessToken(accessToken, refreshTokenClaims)

        val data = checkValidRefreshToken(refreshToken)

        return jwtProvider.receiveToken(data.uuid, data.authority)
    }

    private fun checkValidAccessToken(accessToken: String, refreshTokenClaims: Jws<Claims>) {
        if (refreshTokenClaims.header[Header.JWT_TYPE] != JwtProvider.REFRESH || getClaims(accessToken))
            throw InvalidTokenException
    }

    private fun checkValidRefreshToken(refreshToken: String): RefreshTokenEntity {
        return refreshTokenRepository.findByToken(refreshToken).orElseThrow {
            throw UnexpectedTokenException
        }
    }

    private fun getClaims(token: String): Boolean {
        return try {
            val key: Key = Keys.hmacShaKeyFor(jwtProperties.key.toByteArray(StandardCharsets.UTF_8))
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
            false
        } catch (e: Exception) {
            when (e) {
                is InvalidClaimException -> true
                is ExpiredJwtException -> false
                is JwtException -> true
                else -> throw InternalServerErrorException
            }
        }
    }
}