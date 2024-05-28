package newjeans.bunnies.newjeansbunnies.global.security.jwt


import io.jsonwebtoken.Header
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys

import newjeans.bunnies.newjeansbunnies.domain.auth.RefreshTokenEntity
import newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.TokenDto
import newjeans.bunnies.newjeansbunnies.domain.auth.repository.RefreshTokenRepository
import newjeans.bunnies.newjeansbunnies.domain.auth.type.Authority

import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

import java.nio.charset.StandardCharsets
import java.security.Key
import java.time.LocalDateTime
import java.util.*


@Component
@Configuration
class JwtProvider(
    private val jwtProperties: JwtProperties,
    private val refreshTokenRepository: RefreshTokenRepository
) {
    companion object {
        const val ACCESS = "access"
        const val REFRESH = "refresh"
        const val AUTHORITY = "authority"
    }

    private val accessKey: Key = Keys.hmacShaKeyFor(jwtProperties.accessSecretKey.toByteArray(StandardCharsets.UTF_8))
    private val refreshKey: Key = Keys.hmacShaKeyFor(jwtProperties.refreshSecretKey.toByteArray(StandardCharsets.UTF_8))

    fun receiveToken(uuid: String, authority: Authority) = TokenDto(
        accessToken = generateJwtAccessToken(uuid, authority.name),
        expiredAt = LocalDateTime.now().plusSeconds(jwtProperties.accessExp),
        refreshToken = generateJwtRefreshToken(uuid, authority),
        authority = authority.name
    )

    private fun generateJwtAccessToken(uuid: String, authority: String): String {
        return Jwts.builder()
            .signWith(accessKey, SignatureAlgorithm.HS256)
            .setHeaderParam(Header.JWT_TYPE, ACCESS)
            .setId(uuid)
            .claim(AUTHORITY, authority)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + jwtProperties.accessExp * 1000))
            .compact()
    }

    private fun generateJwtRefreshToken(uuid: String, authority: Authority): String {

        val token = Jwts.builder()
            .signWith(refreshKey, SignatureAlgorithm.HS256)
            .setHeaderParam(Header.JWT_TYPE, REFRESH)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + jwtProperties.refreshExp * 1000))
            .compact()

        val refreshToken = RefreshTokenEntity(
            token = token,
            id = uuid,
            authority = authority,
            expirationTime = jwtProperties.refreshExp / 1000
        )

        refreshTokenRepository.save(refreshToken)

        return token
    }
}