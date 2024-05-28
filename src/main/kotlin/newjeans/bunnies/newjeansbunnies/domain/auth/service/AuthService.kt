package newjeans.bunnies.newjeansbunnies.domain.auth.service

import newjeans.bunnies.newjeansbunnies.domain.post.service.PostService
import newjeans.bunnies.newjeansbunnies.domain.user.error.exception.BlankUserIdException
import newjeans.bunnies.newjeansbunnies.domain.user.repository.UserRepository
import newjeans.bunnies.newjeansbunnies.domain.user.service.UserService
import newjeans.bunnies.newjeansbunnies.global.error.exception.InvalidRoleException
import newjeans.bunnies.newjeansbunnies.global.response.StatusResponseDto
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service

@Configuration
@Service
class AuthService(
    private val userRepository: UserRepository,
    private val userService: UserService,
    private val postService: PostService,
) {

    // 계정 비활성화
    fun userDelete(userId: String, authorizedUser: String?): StatusResponseDto {
        if (userId.isBlank()) throw BlankUserIdException

        if (userId != authorizedUser) throw InvalidRoleException

        val user = userService.checkExistUserId(userId)

        val posts = postService.getPostByUserId(userId)

        posts.map { postData ->
            postService.disabledPost(postData.id, authorizedUser)
        }

        user.state = false

        userRepository.save(user)

        return StatusResponseDto(204, "계정이 삭제 되었습니다.")
    }
}