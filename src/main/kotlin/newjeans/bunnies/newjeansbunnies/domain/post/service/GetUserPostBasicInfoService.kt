package newjeans.bunnies.newjeansbunnies.domain.post.service

import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.NotExistUserIdException
import newjeans.bunnies.newjeansbunnies.domain.post.PostEntity
import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response.GetPostBasicResponseDto
import newjeans.bunnies.newjeansbunnies.domain.post.error.exception.NotExistPostIdException
import newjeans.bunnies.newjeansbunnies.domain.post.repository.PostImageRepository
import newjeans.bunnies.newjeansbunnies.domain.post.repository.PostRepository
import newjeans.bunnies.newjeansbunnies.domain.user.service.UserDataService
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service


@Service
@Configuration
class GetUserPostBasicInfoService(
    private val postRepository: PostRepository,
    private val postImageRepository: PostImageRepository,
    private val userDataService: UserDataService

) {
    suspend fun execute(userId: String, createDate: String): List<GetPostBasicResponseDto>{

        return getPostDateList(userId, createDate).map {
            GetPostBasicResponseDto(
                uuid = it.uuid,
                userId = it.userId,
                body = it.body,
                createDate = it.createDate,
                good = it.good,
                images = getPostImages(it.uuid),
                userImage = userDataService.getUserImage(it.userId).imageURL
            )
        }
    }

    suspend fun getPostDateList(userId: String, createDate: String): List<PostEntity>{
        return postRepository.findTop10ByUserIdAndCreateDateBefore(userId, createDate).orElseThrow {
            throw NotExistUserIdException
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