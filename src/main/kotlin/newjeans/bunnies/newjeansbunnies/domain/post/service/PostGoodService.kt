package newjeans.bunnies.newjeansbunnies.domain.post.service


import newjeans.bunnies.newjeansbunnies.domain.post.PostGoodEntity
import newjeans.bunnies.newjeansbunnies.domain.post.PostEntity
import newjeans.bunnies.newjeansbunnies.domain.post.error.exception.NotExistIdException
import newjeans.bunnies.newjeansbunnies.domain.post.error.exception.PostNotFoundException
import newjeans.bunnies.newjeansbunnies.domain.post.repository.PostGoodRepository
import newjeans.bunnies.newjeansbunnies.domain.post.repository.PostRepository
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
    fun execute(postId: String, userId: String) {
        val postData = postRepository.findById(postId).orElseThrow {
            throw PostNotFoundException
        }

        userRepository.findById(userId).orElseThrow {
            throw NotExistIdException
        }

        val goodStatus = postGoodRepository.existsByUserIdAndPostId(userId, postId)
        if (goodStatus) {
            postGoodRepository.deleteByUserIdAndPostId(postId = postId, userId = userId)
        } else {
            postGoodRepository.save(PostGoodEntity(postId = postId, userId = userId))
        }
        val goodCount = postGoodRepository.countByPostId(postId)

        postRepository.save(
            PostEntity(
                uuid = postData.uuid,
                id = postData.id,
                body = postData.body,
                createDate = postData.createDate,
                good = goodCount
            )
        )
    }

}