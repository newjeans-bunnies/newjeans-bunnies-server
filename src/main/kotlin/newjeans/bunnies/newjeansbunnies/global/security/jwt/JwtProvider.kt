package newjeans.bunnies.newjeansbunnies.global.security.jwt

import io.jsonwebtoken.Header
import io.jsonwebtoken.SignatureAlgorithm
import java.util.Date
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import newjeans.bunnies.newjeansbunnies.domain.auth.RefreshTokenEntity
import newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.TokenDto
import newjeans.bunnies.newjeansbunnies.domain.auth.repository.RefreshTokenRepository
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.security.Key
import java.time.LocalDateTime

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

    private val key: Key = Keys.hmacShaKeyFor(jwtProperties.key.toByteArray(StandardCharsets.UTF_8))

    fun receiveToken(id: String, authority: String) = TokenDto(
        accessToken = generateJwtAccessToken(id, authority),
        expiredAt = LocalDateTime.now().plusSeconds(jwtProperties.accessExp.toLong()),
        refreshToken = generateJwtRefreshToken(id, authority),
        authority = authority
    )

    private fun generateJwtAccessToken(id: String, authority: String): String {
        return Jwts.builder()
            .signWith(key, SignatureAlgorithm.HS256)
            .setHeaderParam(Header.JWT_TYPE, ACCESS)
            .setId(id)
            .claim(AUTHORITY, authority)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + jwtProperties.accessExp * 1000))
            .compact()
    }

    private fun generateJwtRefreshToken(id: String, authority: String): String {

        val token = Jwts.builder()
            .signWith(key, SignatureAlgorithm.HS256)
            .setHeaderParam(Header.JWT_TYPE, REFRESH)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + jwtProperties.refreshExp * 1000))
            .compact()

        val refreshToken = RefreshTokenEntity(
            token = token,
            id = id,
            authority = authority,
            expirationTime = jwtProperties.refreshExp / 1000
        )

        refreshTokenRepository.save(refreshToken)

        return token
    }
}