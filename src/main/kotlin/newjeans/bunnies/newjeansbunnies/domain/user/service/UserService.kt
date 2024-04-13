package newjeans.bunnies.newjeansbunnies.domain.user.service

import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.NotExistUserIdException
import newjeans.bunnies.newjeansbunnies.domain.user.repository.UserRepository
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service

@Service
@Configuration
class UserService(
    private val userRepository: UserRepository
) {
    fun checkExistUserId(userId: String) {
        val user = userRepository.findByUserId(userId)
            .orElseThrow {
                throw NotExistUserIdException
            }
        // DB에서 대소문자 구분을 못해서 이쪽에서 구분
        if (userId != user.userId)
            throw NotExistUserIdException
    }
}