package newjeans.bunnies.newjeansbunnies.domain.post.service


import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response.GetPostBasicResponseDto
import newjeans.bunnies.newjeansbunnies.domain.post.error.exception.NotExistPostIdException
import newjeans.bunnies.newjeansbunnies.domain.post.repository.PostRepository

import org.springframework.context.annotation.Configuration


@Configuration
class GetPostBasicService(
    private val postRepository: PostRepository
) {
    fun execute(date: String): List<GetPostBasicResponseDto> {

        val postListData = postRepository.findTop10ByCreateDateBefore(date).orElseThrow {
            throw NotExistPostIdException
        }


        return postListData.map {
            GetPostBasicResponseDto(
                uuid = it.uuid,
                id = it.id,
                body = it.body,
                good = it.good,
                createDate = it.createDate
            )
        }
    }
}