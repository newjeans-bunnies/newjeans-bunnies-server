package newjeans.bunnies.newjeansbunnies.domain.user.service


import newjeans.bunnies.newjeansbunnies.domain.user.controller.dto.response.UserDataDetailsResponseDto
import newjeans.bunnies.newjeansbunnies.domain.user.repository.UserRepository
import newjeans.bunnies.newjeansbunnies.global.error.exception.InvalidTokenException
import newjeans.bunnies.newjeansbunnies.global.security.jwt.JwtParser
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service


@Service
@Configuration
class UserDataDetailsService(
    private val userRepository: UserRepository,
    private val jwtParser: JwtParser
) {

    private val log: Logger = LoggerFactory.getLogger(UserDataDetailsService::class.java)


    companion object {
        private const val PREFIX = "Bearer "
    }

    suspend fun execute(token: String): UserDataDetailsResponseDto {
        log.info(token)
        if (token.startsWith(PREFIX)) {
            val claims = jwtParser.getClaims(token.removePrefix(PREFIX))
            try {
                val id: String = claims.body["jti"].toString()
                val userData = userRepository.findById(id).orElseThrow {
                    throw InvalidTokenException
                }
                log.info(id)
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