package newjeans.bunnies.newjeansbunnies.domain.user.service


import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.NotExistUserIdException
import newjeans.bunnies.newjeansbunnies.domain.user.controller.dto.response.UserDataDetailsResponseDto
import newjeans.bunnies.newjeansbunnies.domain.user.repository.UserRepository
import newjeans.bunnies.newjeansbunnies.global.error.exception.InvalidRoleException
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

    fun execute(id: String, token: String): UserDataDetailsResponseDto {

        val userData = userRepository.findByUserId(id).orElseThrow {
            throw NotExistUserIdException
        }

        if (token.startsWith(PREFIX)) {
            val claims = jwtParser.getClaims(token.substring(PREFIX.length))
            println(claims.body["jti"])
            println(userData.uuid)
            if (claims.body["jti"] != userData.uuid)
                throw InvalidRoleException

            return UserDataDetailsResponseDto(
                id = userData.userId,
                country = userData.country,
                imageUrl = userData.imageUrl,
                phoneNumber = userData.phoneNumber
            )
        }

        throw InvalidTokenException
    }
}