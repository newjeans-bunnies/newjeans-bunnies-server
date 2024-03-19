package newjeans.bunnies.newjeansbunnies.domain.post.service


import newjeans.bunnies.newjeansbunnies.domain.post.PostEntity
import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response.GetPostBasicResponseDto
import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response.GetPostDetailResponseDto
import newjeans.bunnies.newjeansbunnies.domain.post.error.exception.NotExistPostIdException
import newjeans.bunnies.newjeansbunnies.domain.post.repository.PostGoodRepository
import newjeans.bunnies.newjeansbunnies.domain.post.repository.PostImageRepository
import newjeans.bunnies.newjeansbunnies.domain.post.repository.PostRepository
import newjeans.bunnies.newjeansbunnies.domain.user.service.UserDataService

import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service


@Configuration
@Service
class GetPostService(
    private val postRepository: PostRepository,
    private val postGoodRepository: PostGoodRepository,
    private val postImageRepository: PostImageRepository,
    private val userDataService: UserDataService,
) {
    suspend fun getPostBasicInfo(uuid: String): GetPostBasicResponseDto {
        val postData = getUserData(uuid)

        return GetPostBasicResponseDto(
            uuid = postData.uuid,
            userId = postData.userId,
            body = postData.body,
            createDate = postData.createDate,
            good = postData.good,
            images = getPostImages(postData.uuid),
            userImage = userDataService.getUserImage(postData.userId).imageURL
        )
    }

    suspend fun getPostDetail(uuid: String, userId: String): GetPostDetailResponseDto {
        val postData = getUserData(uuid)

        return GetPostDetailResponseDto(
            uuid = postData.uuid,
            userId = postData.userId,
            body = postData.body,
            createDate = postData.createDate,
            good = postData.good,
            goodStatus = postGoodRepository.existsByUserIdAndPostId(userId, uuid),
            images = getPostImages(postData.uuid),
            userImage = userDataService.getUserImage(postData.userId).imageURL
        )
    }

    suspend fun getUserData(uuid: String): PostEntity {
        return postRepository.findByUuid(uuid).orElseThrow {
            throw NotExistPostIdException
        }
    }

    private suspend fun getPostImages(postId: String): List<String> {
        return postImageRepository.findByPostId(postId).orElseThrow {
            throw NotExistPostIdException
        }.map {
            it.imageUrl
        }
    }
}