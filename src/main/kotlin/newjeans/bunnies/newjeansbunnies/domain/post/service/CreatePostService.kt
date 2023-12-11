package newjeans.bunnies.newjeansbunnies.domain.post.service


import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.NotExistUserIdException
import newjeans.bunnies.newjeansbunnies.domain.post.PostEntity
import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.request.PostRequestDto
import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response.PostResponseDto
import newjeans.bunnies.newjeansbunnies.domain.post.repository.PostRepository
import newjeans.bunnies.newjeansbunnies.domain.user.repository.UserRepository
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service

import java.time.LocalDateTime
import java.util.*


@Configuration
@Service
class CreatePostService(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) {
    fun execute(postData: PostRequestDto): PostResponseDto {

        userRepository.findByUserId(postData.id).orElseThrow {
            throw NotExistUserIdException
        }

        val postEntity = PostEntity(
            uuid = UUID.randomUUID().toString(),
            userId = postData.id,
            body = postData.body,
            createDate = LocalDateTime.now().toString(),
            good = 0
        )

        postRepository.save(postEntity)
        return PostResponseDto(
            postId = postEntity.uuid,
            createDate = postEntity.createDate
        )
    }
}