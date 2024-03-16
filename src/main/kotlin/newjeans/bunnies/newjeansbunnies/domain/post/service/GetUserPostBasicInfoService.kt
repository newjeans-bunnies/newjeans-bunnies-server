package newjeans.bunnies.newjeansbunnies.domain.post.service

import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.NotExistUserIdException
import newjeans.bunnies.newjeansbunnies.domain.post.PostEntity
import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response.GetPostBasicResponseDto
import newjeans.bunnies.newjeansbunnies.domain.post.repository.PostRepository
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service


@Service
@Configuration
class GetUserPostBasicInfoService(
    private val postRepository: PostRepository
) {
    suspend fun execute(userId: String, createDate: String): List<GetPostBasicResponseDto>{

        return getPostDateList(userId, createDate).map {
            GetPostBasicResponseDto(
                uuid = it.uuid,
                userId = it.userId,
                body = it.body,
                createDate = it.createDate,
                good = it.good
            )
        }
    }

    suspend fun getPostDateList(userId: String, createDate: String): List<PostEntity>{
        return postRepository.findTop10ByUserIdAndCreateDateBefore(userId, createDate).orElseThrow {
            throw NotExistUserIdException
        }
    }
}