package newjeans.bunnies.newjeansbunnies.domain.post.service


import newjeans.bunnies.newjeansbunnies.domain.post.PostEntity
import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response.GetPostBasicResponseDto
import newjeans.bunnies.newjeansbunnies.domain.post.error.exception.NotExistPostIdException
import newjeans.bunnies.newjeansbunnies.domain.post.repository.PostImageRepository
import newjeans.bunnies.newjeansbunnies.domain.post.repository.PostRepository
import newjeans.bunnies.newjeansbunnies.domain.user.service.UserDataService

import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort


@Configuration
class GetPostBasicInfoService(
    private val postRepository: PostRepository,
    private val postImageRepository: PostImageRepository,
    private val userDataService: UserDataService
) {
    suspend fun execute(date: String): List<GetPostBasicResponseDto> {

        return getPostListData(date).map {
            GetPostBasicResponseDto(
                uuid = it.uuid,
                userId = it.userId,
                body = it.body,
                good = it.good,
                createDate = it.createDate,
                images = getPostImages(it.uuid),
                userImage = userDataService.getUserImage(it.userId).imageURL
            )
        }
    }

    private suspend fun getPostListData(date: String): List<PostEntity> {
        return postRepository.findByCreateDateBeforeOrderByCreateDateDesc(
            date, PageRequest.of(
                0, 10, Sort.by(
                    Sort.Direction.DESC, "createDate"
                )
            )
        ).orElseThrow {
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