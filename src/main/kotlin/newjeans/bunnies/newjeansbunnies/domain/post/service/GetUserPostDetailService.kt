package newjeans.bunnies.newjeansbunnies.domain.post.service

import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.NotExistUserIdException
import newjeans.bunnies.newjeansbunnies.domain.post.PostEntity
import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response.GetPostDetailResponseDto
import newjeans.bunnies.newjeansbunnies.domain.post.repository.PostGoodRepository
import newjeans.bunnies.newjeansbunnies.domain.post.repository.PostRepository
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service

@Service
@Configuration
class GetUserPostDetailService(
    private val postRepository: PostRepository,
    private val postGoodRepository: PostGoodRepository
) {
    suspend fun execute(date: String, userId: String): List<GetPostDetailResponseDto> {

        return getPostDataList(date, userId).map {
            GetPostDetailResponseDto(
                uuid = it.uuid,
                userId = it.userId,
                body = it.body,
                good = it.good,
                goodStatus = postGoodRepository.existsByUserIdAndPostId(userId, it.uuid),
                createDate = it.createDate
            )
        }
    }

    private suspend fun getPostDataList(date: String, userId: String): List<PostEntity>{
        return postRepository.findTop10ByUserIdAndCreateDateBefore(userId, date).orElseThrow {
            throw NotExistUserIdException
        }
    }
}