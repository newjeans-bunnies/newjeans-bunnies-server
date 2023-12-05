package newjeans.bunnies.newjeansbunnies.domain.post.service

import newjeans.bunnies.newjeansbunnies.domain.post.PostEntity
import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.request.PostRequestDto
import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response.PostResponseDto
import newjeans.bunnies.newjeansbunnies.domain.post.error.exception.NotExistIdException
import newjeans.bunnies.newjeansbunnies.domain.post.repository.PostRepository
import newjeans.bunnies.newjeansbunnies.domain.user.repository.UserRepository
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Configuration
@Service
class PostService(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) {
    fun makePost(postData: PostRequestDto): PostResponseDto {

        userRepository.findById(postData.id).orElseThrow {
            throw NotExistIdException
        }

        val postEntity = PostEntity(
            uuid = UUID.randomUUID().toString(),
            id = postData.id,
            title = postData.title,
            body = postData.body,
            createDate = LocalDateTime.now().toString(),
            good = 0
        )

        postRepository.save(postEntity)
        return PostResponseDto(
            uuid = postEntity.uuid,
            createDate = postEntity.createDate
        )
    }

//    fun getPost(date: String): List<PostResponseDto> {
//
//    }
}