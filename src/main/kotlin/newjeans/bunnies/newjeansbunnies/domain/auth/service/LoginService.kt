package newjeans.bunnies.newjeansbunnies.domain.auth.service


import newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.TokenDto
import newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.request.LoginRequestDto
import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.InvalidPasswordException
import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.NotExistIdException
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
    fun execute(data: LoginRequestDto): TokenDto {

        val userData = userRepository.findByUserId(data.userId).orElseThrow {
            throw NotExistIdException
        }

        val password = userData.password

        if (!passwordEncoder.matches(data.password, password)) throw InvalidPasswordException

        return jwtProvider.receiveToken(userData.uuid, "USER")
    }
}