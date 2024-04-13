package newjeans.bunnies.newjeansbunnies.domain.user.service


import jakarta.transaction.Transactional
import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.NotExistUserIdException
import newjeans.bunnies.newjeansbunnies.domain.auth.repository.RefreshTokenRepository
import newjeans.bunnies.newjeansbunnies.domain.user.repository.UserRepository
import newjeans.bunnies.newjeansbunnies.global.response.StatusResponseDto
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service


@Service
@Configuration
class DeleteUserService(
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val deleteUserImageService: DeleteUserImageService
) {
    @Transactional
    suspend fun execute(userId: String): StatusResponseDto {
        val userData = userRepository.findByUserId(userId).orElseThrow {
            throw NotExistUserIdException
        }
        //게시물 지우기
//        deletePostService.deletePostByUserId(userId)

        //유저 지우기
        userRepository.deleteByUserId(userId)
        //유저
        deleteUserImageService.execute(userData.uuid)

        refreshTokenRepository.deleteById(userData.uuid)

        return StatusResponseDto(
            status = 204,
            message = "계정 삭제됨"
        )
    }
}