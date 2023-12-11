package newjeans.bunnies.newjeansbunnies.domain.parents_comment.service


import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.NotExistUserIdException
import newjeans.bunnies.newjeansbunnies.domain.parents_comment.ParentsCommentEntity
import newjeans.bunnies.newjeansbunnies.domain.parents_comment.ParentsCommentGoodEntity
import newjeans.bunnies.newjeansbunnies.domain.parents_comment.controller.dto.response.ParentsCommentGoodResponseDto
import newjeans.bunnies.newjeansbunnies.domain.parents_comment.error.exception.NotExistParentsCommentIdException
import newjeans.bunnies.newjeansbunnies.domain.parents_comment.repository.ParentsCommentGoodRepository
import newjeans.bunnies.newjeansbunnies.domain.parents_comment.repository.ParentsCommentRepository
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
    fun execute(commentId: String, userId: String): ParentsCommentGoodResponseDto {

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
                userId = commentData.userId,
                body = commentData.body,
                good = goodCount,
                postId = commentData.postId,
                createDate = commentData.createDate
            )
        )

        return ParentsCommentGoodResponseDto(
            commentId = commentId,
            good = goodCount,
            goodStatus = !goodStatus
        )

    }
}