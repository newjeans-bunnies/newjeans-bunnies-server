package newjeans.bunnies.newjeansbunnies.domain.user.service

import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.NotExistUserIdException
import newjeans.bunnies.newjeansbunnies.domain.user.controller.dto.response.UserImageResponseDto
import newjeans.bunnies.newjeansbunnies.domain.user.repository.UserRepository
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service

@Service
@Configuration
class UserDataService(
    private val userRepository: UserRepository
) {
    fun getUserImage(userId: String): UserImageResponseDto {
        val imageURL = userRepository.findByUserId(userId).orElseThrow {
            throw NotExistUserIdException
        }.imageUrl

        return UserImageResponseDto(imageURL)
    }
}