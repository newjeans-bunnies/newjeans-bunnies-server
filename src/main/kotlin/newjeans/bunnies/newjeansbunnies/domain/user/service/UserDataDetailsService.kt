package newjeans.bunnies.newjeansbunnies.domain.user.service


import newjeans.bunnies.newjeansbunnies.domain.user.controller.dto.response.UserDataDetailsResponseDto
import newjeans.bunnies.newjeansbunnies.domain.user.repository.UserRepository
import newjeans.bunnies.newjeansbunnies.global.error.exception.InvalidTokenException
import newjeans.bunnies.newjeansbunnies.global.security.jwt.JwtParser
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service


@Service
@Configuration
class UserDataDetailsService(
    private val userRepository: UserRepository,
    private val jwtParser: JwtParser
) {

    companion object {
        private const val PREFIX = "Bearer "
    }

    fun execute(token: String): UserDataDetailsResponseDto {

        if (token.startsWith(PREFIX)) {
            val claims = jwtParser.getClaims(token.removePrefix(PREFIX))
            try {
                val id: String = claims.body["jti"].toString()
                val userData = userRepository.findByUuid(id).orElseThrow {
                    throw InvalidTokenException
                }
                return UserDataDetailsResponseDto(
                    id = userData.userId,
                    country = userData.country,
                    imageUrl = userData.imageUrl,
                    phoneNumber = userData.phoneNumber
                )
            } catch (e: Exception) {
                throw InvalidTokenException
            }
        }
        throw InvalidTokenException
    }
}