package newjeans.bunnies.newjeansbunnies.domain.post.service

import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.NotExistUserIdException
import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response.GetPostBasicResponseDto
import newjeans.bunnies.newjeansbunnies.domain.post.repository.PostRepository
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service


@Service
@Configuration
class GetUserPostBasicInfoService(
    private val postRepository: PostRepository
) {
    fun execute(userId: String, createDate: String): List<GetPostBasicResponseDto>{
        val postDataList = postRepository.findTop10ByUserIdAndCreateDateBefore(userId, createDate).orElseThrow {
            throw NotExistUserIdException
        }

        return postDataList.map {
            GetPostBasicResponseDto(
                uuid = it.uuid,
                userId = it.userId,
                body = it.body,
                createDate = it.createDate,
                good = it.good
            )
        }
    }
}