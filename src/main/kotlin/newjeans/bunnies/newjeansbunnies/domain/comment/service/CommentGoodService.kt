package newjeans.bunnies.newjeansbunnies.domain.comment.service

import jakarta.transaction.Transactional
import newjeans.bunnies.newjeansbunnies.domain.comment.CommentEntity
import newjeans.bunnies.newjeansbunnies.domain.comment.CommentGoodEntity
import newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.response.CommentGoodResponseDto
import newjeans.bunnies.newjeansbunnies.domain.comment.error.exception.InactiveCommentException
import newjeans.bunnies.newjeansbunnies.domain.comment.error.exception.NotExistCommentIdException
import newjeans.bunnies.newjeansbunnies.domain.comment.repository.CommentGoodRepository
import newjeans.bunnies.newjeansbunnies.domain.comment.repository.CommentRepository
import newjeans.bunnies.newjeansbunnies.domain.user.service.UserService
import newjeans.bunnies.newjeansbunnies.global.error.exception.InvalidRoleException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@Service
@Component
class CommentGoodService(
    private val commentGoodRepository: CommentGoodRepository,
    private val commentRepository: CommentRepository,
    private val userService: UserService
) {
    fun getCommentGoodState(userId: String, commentId: String): Boolean {
        return commentGoodRepository.existsByUserIdAndCommentId(userId, commentId)
    }

    @Transactional
    fun goodComment(commentId: String, authorizedUser: String?): CommentGoodResponseDto {
        val userId = authorizedUser ?: throw InvalidRoleException
        userService.checkExistUserId(userId)

        val comment = getComment(commentId)

        if (!comment.state) throw InactiveCommentException

        if (getCommentGoodState(userId, commentId)) {
            commentGoodRepository.deleteByUserIdAndCommentId(userId, commentId)

            comment.goodCounts -= 1
            commentRepository.save(comment)

            return CommentGoodResponseDto(commentId, false, comment.goodCounts)
        }

        val commentGood = CommentGoodEntity(
            userId = userId, commentId = commentId
        )

        commentGoodRepository.save(commentGood)

        comment.goodCounts += 1
        commentRepository.save(comment)

        return CommentGoodResponseDto(commentId, true, comment.goodCounts)
    }

    fun disabledGoodComment(commentId: String) {
        if (!commentRepository.existsById(commentId)) throw NotExistCommentIdException

        val goodComments = commentGoodRepository.findByCommentId(commentId).orElseThrow { NotExistCommentIdException }

        goodComments.map { goodComment ->
            goodComment.state = false
            commentGoodRepository.save(goodComment)
        }
    }

    fun enabledGoodComment(commentId: String) {
        if (!commentRepository.existsById(commentId)) throw NotExistCommentIdException

        val goodComments = commentGoodRepository.findByCommentId(commentId).orElseThrow { NotExistCommentIdException }

        goodComments.map { goodComment ->
            goodComment.state = true
            commentGoodRepository.save(goodComment)
        }
    }

    private fun getComment(commentId: String): CommentEntity {
        return commentRepository.findByIdOrNull(commentId) ?: throw NotExistCommentIdException
    }

}