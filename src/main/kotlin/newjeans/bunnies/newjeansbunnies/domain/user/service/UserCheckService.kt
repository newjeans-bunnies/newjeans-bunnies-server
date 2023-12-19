package newjeans.bunnies.newjeansbunnies.domain.user.service

import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.ExistIdException
import newjeans.bunnies.newjeansbunnies.domain.user.repository.UserRepository
import newjeans.bunnies.newjeansbunnies.global.response.StatusResponseDto
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service

@Service
@Configuration
class UserCheckService(
    private val userRepository: UserRepository
) {
    fun execute(userId: String): StatusResponseDto{
        if(userRepository.findByUserId(userId).isPresent)
            throw ExistIdException

        return StatusResponseDto(
            status = 200,
            message = "Ok"
        )
    }
}