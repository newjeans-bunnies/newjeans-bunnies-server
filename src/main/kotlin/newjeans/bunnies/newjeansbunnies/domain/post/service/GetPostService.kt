package newjeans.bunnies.newjeansbunnies.domain.post.service


import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response.GetPostBasicResponseDto
import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response.GetPostDetailResponseDto
import newjeans.bunnies.newjeansbunnies.domain.post.error.exception.NotExistPostIdException
import newjeans.bunnies.newjeansbunnies.domain.post.repository.PostGoodRepository
import newjeans.bunnies.newjeansbunnies.domain.post.repository.PostRepository

import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service


@Configuration
@Service
class GetPostService(
    private val postRepository: PostRepository,
    private val postGoodRepository: PostGoodRepository
) {
    fun getPostBasicInfo(uuid: String): GetPostBasicResponseDto {
        val postData = postRepository.findById(uuid).orElseThrow {
            throw NotExistPostIdException
        }

        return GetPostBasicResponseDto(
            uuid = postData.uuid,
            id = postData.userId,
            body = postData.body,
            createDate = postData.createDate,
            good = postData.good
        )
    }

    fun getPostDetail(uuid: String, userId: String): GetPostDetailResponseDto {
        val postData = postRepository.findById(uuid).orElseThrow {
            throw NotExistPostIdException
        }

        return GetPostDetailResponseDto(
            uuid = postData.uuid,
            id = postData.userId,
            body = postData.body,
            createDate = postData.createDate,
            good = postData.good,
            goodStatus = postGoodRepository.existsByUserIdAndPostId(userId, uuid)
        )
    }
}