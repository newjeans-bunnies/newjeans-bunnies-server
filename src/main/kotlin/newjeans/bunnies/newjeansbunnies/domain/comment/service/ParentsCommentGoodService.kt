package newjeans.bunnies.newjeansbunnies.domain.comment.service


import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.NotExistUserIdException
import newjeans.bunnies.newjeansbunnies.domain.comment.ParentsCommentEntity
import newjeans.bunnies.newjeansbunnies.domain.comment.ParentsCommentGoodEntity
import newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.response.CommentGoodResponseDto
import newjeans.bunnies.newjeansbunnies.domain.comment.error.exception.NotExistParentsCommentIdException
import newjeans.bunnies.newjeansbunnies.domain.comment.repository.ParentsCommentGoodRepository
import newjeans.bunnies.newjeansbunnies.domain.comment.repository.ParentsCommentRepository
import newjeans.bunnies.newjeansbunnies.domain.user.repository.UserRepository

import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Configuration
class ParentsCommentGoodService(
    private val parentsCommentGoodRepository: ParentsCommentGoodRepository,
    private val parentsCommentRepository: ParentsCommentRepository,
    private val userRepository: UserRepository
) {

    @Transactional
    fun execute(commentId: String, userId: String): CommentGoodResponseDto {

        val commentData = parentsCommentRepository.findById(commentId).orElseThrow {
            throw NotExistParentsCommentIdException
        }

        userRepository.findByUserId(userId).orElseThrow {
            throw NotExistUserIdException
        }


        val goodStatus = parentsCommentGoodRepository.existsByParentsCommentIdAndUserId(commentId, userId)

        if (goodStatus) {
            parentsCommentGoodRepository.deleteByParentsCommentIdAndUserId(commentId, userId)
        } else {
            parentsCommentGoodRepository.save(ParentsCommentGoodEntity(parentsCommentId = commentId, userId = userId))
        }

        val goodCount = parentsCommentGoodRepository.countByParentsCommentId(commentId)

        parentsCommentRepository.save(
            ParentsCommentEntity(
                uuid = commentData.uuid,
                id = commentData.id,
                body = commentData.body,
                good = goodCount,
                postId = commentData.postId,
                createDate = commentData.createDate
            )
        )

        return CommentGoodResponseDto(
            commentId = commentId,
            good = goodCount,
            goodStatus = !goodStatus
        )

    }
}