package newjeans.bunnies.newjeansbunnies.domain.post.service


import newjeans.bunnies.newjeansbunnies.domain.post.PostEntity
import newjeans.bunnies.newjeansbunnies.domain.post.PostGoodEntity
import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response.PostGoodResponseDto
import newjeans.bunnies.newjeansbunnies.domain.post.error.exception.DisabledPostException
import newjeans.bunnies.newjeansbunnies.domain.post.error.exception.NotExistPostIdException
import newjeans.bunnies.newjeansbunnies.domain.post.repository.PostGoodRepository
import newjeans.bunnies.newjeansbunnies.domain.post.repository.PostRepository
import newjeans.bunnies.newjeansbunnies.domain.user.error.exception.NotExistUserIdException
import newjeans.bunnies.newjeansbunnies.domain.user.service.UserService
import newjeans.bunnies.newjeansbunnies.global.error.exception.InvalidRoleException
import org.springframework.context.annotation.Configuration
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Configuration
class PostGoodService(
    private val postGoodRepository: PostGoodRepository,
    private val postRepository: PostRepository,
    private val userService: UserService,
) {
    @Transactional
    fun postGood(postId: String, userId: String, authorizedUser: String?): PostGoodResponseDto {

        if (userId != authorizedUser) throw InvalidRoleException

        // 유효 PostId인지 확인
        val postData = getValidPost(postId)

        val post = getValidPost(postId)

        // UserID 대소문자 구분
        userService.checkExistNickname(userId)

        if (getPostGoodState(postId, userId)) {
            postGoodRepository.deleteByUserIdAndPostId(postId = postId, userId = userId)

            post.goodCounts -= 1
            postRepository.save(post)

            return PostGoodResponseDto(
                postId = postData.id, goodCounts = post.goodCounts, goodStatus = false
            )
        }

        val postGood = PostGoodEntity(
            postId = post.id, userId = userId
        )

        postGoodRepository.save(postGood)

        post.goodCounts += 1
        postRepository.save(post)

        return PostGoodResponseDto(
            postId = postData.id, goodCounts = post.goodCounts, goodStatus = true
        )
    }

    fun getPostGoodState(postId: String, userId: String): Boolean {
        return postGoodRepository.existsByUserIdAndPostId(userId, postId)
    }

    fun getValidPost(postId: String): PostEntity {
        val post = postRepository.findByIdOrNull(postId) ?: throw NotExistPostIdException

        // 게시글이 비활성화 상태라면 애러 처리
        if (!post.state) throw DisabledPostException

        return post
    }

    fun disabledPostGood(userId: String) {
        val postGoods = postGoodRepository.findByUserId(userId).orElseThrow { throw NotExistUserIdException }
        postGoods.map {
            it.state = false
            postGoodRepository.save(it)
        }
    }

}