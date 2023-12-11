package newjeans.bunnies.newjeansbunnies.domain.children_comment.service


import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.NotExistUserIdException
import newjeans.bunnies.newjeansbunnies.domain.children_comment.ChildrenCommentEntity
import newjeans.bunnies.newjeansbunnies.domain.children_comment.ChildrenCommentGoodEntity
import newjeans.bunnies.newjeansbunnies.domain.children_comment.controller.dto.response.ChildrenCommentGoodResponseDto
import newjeans.bunnies.newjeansbunnies.domain.children_comment.error.exception.NotExistChildrenCommentIdException
import newjeans.bunnies.newjeansbunnies.domain.children_comment.repository.ChildrenCommentGoodRepository
import newjeans.bunnies.newjeansbunnies.domain.children_comment.repository.ChildrenCommentRepository
import newjeans.bunnies.newjeansbunnies.domain.user.repository.UserRepository
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Configuration
class ChildrenCommentGoodService(
    private val childrenCommentGoodRepository: ChildrenCommentGoodRepository,
    private val childrenCommentRepository: ChildrenCommentRepository,
    private val userRepository: UserRepository
) {
    @Transactional
    fun execute(childrenCommentId: String, userId: String): ChildrenCommentGoodResponseDto {
        val commentData = childrenCommentRepository.findById(childrenCommentId).orElseThrow {
            throw NotExistChildrenCommentIdException
        }

        userRepository.findByUserId(userId).orElseThrow {
            throw NotExistUserIdException
        }

        val goodStatus = childrenCommentGoodRepository.existsByChildrenCommentIdAndUserId(childrenCommentId, userId)

        if (goodStatus) {
            childrenCommentGoodRepository.deleteByChildrenCommentIdAndUserId(childrenCommentId, userId)
        } else {
            childrenCommentGoodRepository.save(
                ChildrenCommentGoodEntity(
                    childrenCommentId = childrenCommentId,
                    userId = userId
                )
            )
        }

        val goodCount = childrenCommentGoodRepository.countByChildrenCommentId(childrenCommentId)

        childrenCommentRepository.save(
            ChildrenCommentEntity(
                uuid = commentData.uuid,
                id = commentData.id,
                body = commentData.body,
                createDate = commentData.createDate,
                parentCommentId = commentData.parentCommentId,
                good = goodCount
            )
        )

        return ChildrenCommentGoodResponseDto(
            commentId = childrenCommentId,
            good = goodCount,
            goodStatus = !goodStatus
        )

    }
}