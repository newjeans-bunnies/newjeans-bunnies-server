package newjeans.bunnies.newjeansbunnies.domain.comment.service

import newjeans.bunnies.newjeansbunnies.domain.comment.repository.CommentGoodRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@Service
@Component
class CommentGoodService(
    private val commentGoodRepository: CommentGoodRepository
) {
    fun getCommentGoodState(userId: String, commentId: String): Boolean{
        return commentGoodRepository.existsByUserIdAndCommentId(userId, commentId)
    }

}