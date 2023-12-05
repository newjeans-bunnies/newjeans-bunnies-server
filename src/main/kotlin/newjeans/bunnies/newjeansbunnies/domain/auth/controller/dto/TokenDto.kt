package newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto

import java.time.LocalDateTime

data class TokenDto(
    val accessToken: String,
    val refreshToken: String,
    val expiredAt: LocalDateTime,
    val authority: String
)