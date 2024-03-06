package newjeans.bunnies.newjeansbunnies.domain.user.service


import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.ExistIdException
import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.ExistPhoneNumberException
import newjeans.bunnies.newjeansbunnies.domain.user.error.exception.RuleViolationUserIdException
import newjeans.bunnies.newjeansbunnies.domain.user.repository.UserRepository
import newjeans.bunnies.newjeansbunnies.global.response.StatusResponseDto

import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service


@Service
@Configuration
class UserCheckService(
    private val userRepository: UserRepository
) {

    private val idRegex = Regex("^([a-zA-Z0-9]){1,12}\$")

    fun userId(userId: String): StatusResponseDto {
        if (userRepository.findByUserId(userId).isPresent && userId == userRepository.findByUserId(userId).get().userId)
            throw ExistIdException

        if(!idPattern(userId))
            throw RuleViolationUserIdException

        return StatusResponseDto(
            status = 200,
            message = "Ok"
        )
    }

    fun idPattern(input: String): Boolean {
        return idRegex.matches(input)
    }

    fun phoneNumber(phoneNumber: String): StatusResponseDto {
        if (userRepository.findByPhoneNumber(phoneNumber).isPresent)
            throw ExistPhoneNumberException

        return StatusResponseDto(
            status = 200,
            message = "Ok"
        )
    }
}