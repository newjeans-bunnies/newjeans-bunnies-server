package newjeans.bunnies.newjeansbunnies.domain.post.service


import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.NotExistUserIdException
import newjeans.bunnies.newjeansbunnies.domain.post.PostEntity
import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response.GetPostDetailResponseDto
import newjeans.bunnies.newjeansbunnies.domain.post.error.exception.NotExistPostIdException
import newjeans.bunnies.newjeansbunnies.domain.post.repository.PostGoodRepository
import newjeans.bunnies.newjeansbunnies.domain.post.repository.PostImageRepository
import newjeans.bunnies.newjeansbunnies.domain.post.repository.PostRepository
import newjeans.bunnies.newjeansbunnies.domain.user.repository.UserRepository
import newjeans.bunnies.newjeansbunnies.domain.user.service.UserDataService
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort


@Configuration
class GetPostDetailService(
    private val postRepository: PostRepository,
    private val postGoodRepository: PostGoodRepository,
    private val userRepository: UserRepository,
    private val postImageRepository: PostImageRepository,
    private val userDataService: UserDataService
) {

    suspend fun execute(date: String, userId: String): List<GetPostDetailResponseDto> {

        checkValidUserId(userId)

        return getPostListData(date).map {
            GetPostDetailResponseDto(
                uuid = it.uuid,
                userId = it.userId,
                body = it.body,
                good = it.good,
                goodStatus = postGoodRepository.existsByUserIdAndPostId(userId, it.uuid),
                createDate = it.createDate,
                images = getPostImages(it.uuid),
                userImage = userDataService.getUserImage(it.userId).imageURL
            )
        }
    }

    private suspend fun getPostListData(date: String): List<PostEntity>{
        return postRepository.findByCreateDateBeforeOrderByCreateDateDesc(date, PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createDate"))).orElseThrow {
            throw NotExistPostIdException
        }
    }

    private suspend fun checkValidUserId(userId: String){
        userRepository.findByUserId(userId).orElseThrow {
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