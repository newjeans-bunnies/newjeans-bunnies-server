package newjeans.bunnies.newjeansbunnies.domain.auth.service

import newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.TokenDto
import newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.request.LoginRequestDto
import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.InvalidPasswordException
import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.NotExistUserIdException
import newjeans.bunnies.newjeansbunnies.domain.user.UserEntity
import newjeans.bunnies.newjeansbunnies.domain.user.repository.UserRepository
import newjeans.bunnies.newjeansbunnies.global.security.jwt.JwtProvider
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
@Configuration
class LoginService(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val jwtProvider: JwtProvider
) {
    suspend fun execute(data: LoginRequestDto): TokenDto {

        val userData = getExistUserId(data.userId)
        checkExistUserId(userData.userId, data.userId)

        matchesPassword(data.password, userData.password)

        return jwtProvider.receiveToken(userData.uuid, userData.authority)
    }

    private suspend fun getExistUserId(userId: String): UserEntity {
        return userRepository.findByUserId(userId).orElseThrow {
            throw NotExistUserIdException
        }
    }

    private suspend fun matchesPassword(password: String, sparePassword: String) {
        if (!passwordEncoder.matches(password, sparePassword)) throw InvalidPasswordException
    }

    private suspend fun checkExistUserId(userId: String, spareUserId: String) {
        if (userId != spareUserId)
            throw NotExistUserIdException
    }
}