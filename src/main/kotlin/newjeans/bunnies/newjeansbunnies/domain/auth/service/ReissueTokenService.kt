package newjeans.bunnies.newjeansbunnies.domain.auth.service

import newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.TokenDto
import newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.response.TokenResponseDto
import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.RefreshTokenNotForundException
import newjeans.bunnies.newjeansbunnies.domain.auth.repository.RefreshTokenRepository
import newjeans.bunnies.newjeansbunnies.global.security.jwt.JwtProvider
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service

@Service
@Configuration
class ReissueTokenService(
    private val jwtProvider: JwtProvider,
    private val refreshTokenRepository: RefreshTokenRepository
) {
    fun execute(token: String): TokenDto {
        val data = refreshTokenRepository.findByToken(token).orElseThrow {
            throw RefreshTokenNotForundException
        }

        return jwtProvider.receiveToken(data.id, data.authority)
    }
}