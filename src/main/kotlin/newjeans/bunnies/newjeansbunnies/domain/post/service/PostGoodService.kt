package newjeans.bunnies.newjeansbunnies.domain.post.service


import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.NotExistUserIdException
import newjeans.bunnies.newjeansbunnies.domain.post.PostEntity
import newjeans.bunnies.newjeansbunnies.domain.post.PostGoodEntity
import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response.PostGoodResponseDto
import newjeans.bunnies.newjeansbunnies.domain.post.error.exception.NotExistPostIdException
import newjeans.bunnies.newjeansbunnies.domain.post.repository.PostGoodRepository
import newjeans.bunnies.newjeansbunnies.domain.post.repository.PostRepository
import newjeans.bunnies.newjeansbunnies.domain.user.UserEntity
import newjeans.bunnies.newjeansbunnies.domain.user.repository.UserRepository
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Configuration
class PostGoodService(
    private val postRepository: PostRepository,
    private val postGoodRepository: PostGoodRepository,
    private val userRepository: UserRepository
) {
    @Transactional
    suspend fun execute(postId: String, userId: String): PostGoodResponseDto {

        val postData = getValidPost(postId)
        val userData = getValidUserId(userId)

        checkExistUserId(userData.userId, userId)

        val goodStatus = postGoodRepository.existsByUserIdAndPostId(userId, postId)
        var goodCount = postData.good

        if (goodStatus) {
            goodCount--
            postGoodRepository.deleteByUserIdAndPostId(postId = postId, userId = userId)
        } else {
            goodCount++
            postGoodRepository.save(PostGoodEntity(postId = postId, userId = userId))
        }


        postRepository.save(
            PostEntity(
                uuid = postData.uuid,
                userId = postData.userId,
                body = postData.body,
                createDate = postData.createDate,
                good = goodCount
            )
        )

        return PostGoodResponseDto(
            postId = postData.uuid,
            good = goodCount,
            goodStatus = !goodStatus
        )
    }

    private suspend fun getValidPost(postId: String): PostEntity{
        return postRepository.findByUuid(postId).orElseThrow {
            throw NotExistPostIdException
        }
    }

    private suspend fun getValidUserId(userId: String): UserEntity{
        return userRepository.findByUserId(userId).orElseThrow {
            throw NotExistUserIdException
        }
    }

    private suspend fun checkExistUserId(userId: String, spareUserId: String){
        if (userId != spareUserId)
            throw NotExistUserIdException
    }

}