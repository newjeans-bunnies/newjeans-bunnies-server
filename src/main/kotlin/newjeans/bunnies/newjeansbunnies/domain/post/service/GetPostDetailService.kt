package newjeans.bunnies.newjeansbunnies.domain.post.service


import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.NotExistUserIdException
import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response.GetPostDetailResponseDto
import newjeans.bunnies.newjeansbunnies.domain.post.error.exception.NotExistPostIdException
import newjeans.bunnies.newjeansbunnies.domain.post.repository.PostGoodRepository
import newjeans.bunnies.newjeansbunnies.domain.post.repository.PostRepository
import newjeans.bunnies.newjeansbunnies.domain.user.repository.UserRepository
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort


@Configuration
class GetPostDetailService(
    private val postRepository: PostRepository,
    private val postGoodRepository: PostGoodRepository,
    private val userRepository: UserRepository
) {

    fun execute(date: String, userId: String): List<GetPostDetailResponseDto> {

        userRepository.findByUserId(userId).orElseThrow {
            throw NotExistUserIdException
        }

//        val postListData = postRepository.findTop10ByCreateDateBefore(date).orElseThrow {
//            throw NotExistPostIdException
//        }
        val postListData = postRepository.findByCreateDateBeforeOrderByCreateDateDesc(date, PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createDate"))).orElseThrow {
            throw NotExistPostIdException
        }


        return postListData.map {
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
}