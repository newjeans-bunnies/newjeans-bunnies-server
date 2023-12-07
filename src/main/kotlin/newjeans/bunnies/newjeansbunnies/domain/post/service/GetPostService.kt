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
    fun execute(date: String): List<GetPostResponseDto>{
        val postListData = postRepository.findByCreateDateBefore(date).orElseThrow {
            throw PostNotFoundException
        }

        return postListData.map {
            GetPostResponseDto(
                uuid = it.uuid,
                id = it.id,
                body = it.body,
                good = it.good,
                createDate = it.createDate
            )
        }
    }
}