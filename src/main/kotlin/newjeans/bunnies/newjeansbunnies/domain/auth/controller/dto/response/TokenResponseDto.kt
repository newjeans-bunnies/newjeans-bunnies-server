package newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.response

import java.time.LocalDateTime

data class TokenResponseDto(
    val accessToken: String,
    val expiredAt: LocalDateTime,
    val authority: String
)
