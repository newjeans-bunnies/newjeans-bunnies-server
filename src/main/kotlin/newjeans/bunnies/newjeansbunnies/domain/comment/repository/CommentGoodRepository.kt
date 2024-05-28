package newjeans.bunnies.newjeansbunnies.domain.comment.repository

import newjeans.bunnies.newjeansbunnies.domain.comment.CommentGoodEntity
import org.springframework.data.repository.CrudRepository
import java.util.*

interface CommentGoodRepository : CrudRepository<CommentGoodEntity, String> {
    fun existsByUserIdAndCommentId(userId: String, commentId: String): Boolean

    fun deleteByUserIdAndCommentId(userId: String, commentId: String)

    fun findByCommentId(commentId: String): Optional<List<CommentGoodEntity>>
}