package newjeans.bunnies.newjeansbunnies.domain.comment.service


import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.NotExistUserIdException
import newjeans.bunnies.newjeansbunnies.domain.comment.ChildrenCommentEntity
import newjeans.bunnies.newjeansbunnies.domain.comment.ChildrenCommentGoodEntity
import newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.response.CommentGoodResponseDto
import newjeans.bunnies.newjeansbunnies.domain.comment.error.exception.NotExistChildrenCommentIdException
import newjeans.bunnies.newjeansbunnies.domain.comment.repository.ChildrenCommentGoodRepository
import newjeans.bunnies.newjeansbunnies.domain.comment.repository.ChildrenCommentRepository
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
    fun execute(commentId: String, userId: String): CommentGoodResponseDto {
        val commentData = childrenCommentRepository.findById(commentId).orElseThrow {
            throw NotExistChildrenCommentIdException
        }

        userRepository.findByUserId(userId).orElseThrow {
            throw NotExistUserIdException
        }

        val goodStatus = childrenCommentGoodRepository.existsByChildrenCommentIdAndUserId(commentId, userId)

        if (goodStatus) {
            childrenCommentGoodRepository.deleteByChildrenCommentIdAndUserId(commentId, userId)
        } else {
            childrenCommentGoodRepository.save(
                ChildrenCommentGoodEntity(
                    childrenCommentId = commentId,
                    userId = userId
                )
            )
        }

        val goodCount = childrenCommentGoodRepository.countByChildrenCommentId(commentId)

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

        return CommentGoodResponseDto(
            commentId = commentId,
            good = goodCount,
            goodStatus = !goodStatus
        )

    }
}