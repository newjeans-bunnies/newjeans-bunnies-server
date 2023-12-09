package newjeans.bunnies.newjeansbunnies.domain.post.service


import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response.GetPostResponseDto
import newjeans.bunnies.newjeansbunnies.domain.post.error.exception.PostNotFoundException
import newjeans.bunnies.newjeansbunnies.domain.post.repository.PostRepository

import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service


@Configuration
@Service
class GetPostService(
    private val postRepository: PostRepository
) {
    fun execute(uuid: String): GetPostResponseDto {
        val postData = postRepository.findById(uuid).orElseThrow {
            throw PostNotFoundException
        }

        return GetPostResponseDto(
            uuid = postData.uuid,
            id = postData.id,
            body = postData.body,
            createDate = postData.createDate,
            good = postData.good
        )
    }
}