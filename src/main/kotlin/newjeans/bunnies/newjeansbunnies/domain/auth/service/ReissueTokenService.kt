package newjeans.bunnies.newjeansbunnies.domain.auth.service


import io.jsonwebtoken.Header

import newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.TokenDto
import newjeans.bunnies.newjeansbunnies.domain.auth.repository.RefreshTokenRepository
import newjeans.bunnies.newjeansbunnies.global.error.exception.InvalidTokenException
import newjeans.bunnies.newjeansbunnies.global.error.exception.UnexpectedTokenException
import newjeans.bunnies.newjeansbunnies.global.security.jwt.JwtParser
import newjeans.bunnies.newjeansbunnies.global.security.jwt.JwtProvider

import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service


@Service
@Configuration
class ReissueTokenService(
    private val jwtProvider: JwtProvider,
    private val jwtParser: JwtParser,
    private val refreshTokenRepository: RefreshTokenRepository,
) {
    fun execute(refreshToken: String): TokenDto {
        val claims = jwtParser.getClaims(refreshToken)
        if(claims.header[Header.JWT_TYPE] != JwtProvider.REFRESH)
            throw InvalidTokenException

        val data = refreshTokenRepository.findByToken(refreshToken).orElseThrow {
            throw UnexpectedTokenException
        }

        return jwtProvider.receiveToken(data.uuid, data.authority)
    }
}