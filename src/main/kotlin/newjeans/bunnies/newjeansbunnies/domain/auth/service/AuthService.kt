package newjeans.bunnies.newjeansbunnies.domain.auth.service

import newjeans.bunnies.newjeansbunnies.domain.user.UserEntity
import newjeans.bunnies.newjeansbunnies.domain.user.repository.UserRepository
import newjeans.bunnies.newjeansbunnies.domain.user.service.UserService
import newjeans.bunnies.newjeansbunnies.global.response.StatusResponseDto
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service

@Configuration
@Service
class AuthService(
    private val userRepository: UserRepository,
    private val userService: UserService
) {

    // 계정 비활성화
    fun userDelete(userId: String): StatusResponseDto {
        val user = userService.checkExistUserId(userId)

        userRepository.save(
            UserEntity(
                uuid = user.uuid,
                authority = user.authority,
                userId = user.userId,
                password = user.password,
                phoneNumber = user.phoneNumber,
                language = user.language,
                imageUrl = user.imageUrl,
                country = user.country,
                birth = user.birth,
                state = false
            )
        )

        return StatusResponseDto(204, "계정이 삭제 되었습니다.")
    }
}