package newjeans.bunnies.newjeansbunnies.domain.user.service


import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.NotExistIdException
import newjeans.bunnies.newjeansbunnies.domain.user.controller.dto.response.UserDataBasicInfoResponseDto
import newjeans.bunnies.newjeansbunnies.domain.user.repository.UserRepository

import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service


@Service
@Configuration
class UserDataBasicInfoService(
    private val userRepository: UserRepository
) {
    fun execute(id: String): UserDataBasicInfoResponseDto {
        val userData = userRepository.findByUserId(id).orElseThrow {
            throw NotExistIdException
        }

        return UserDataBasicInfoResponseDto(
            id = userData.userId,
            country = userData.country,
            imageUrl = userData.imageUrl
        )

    }
}