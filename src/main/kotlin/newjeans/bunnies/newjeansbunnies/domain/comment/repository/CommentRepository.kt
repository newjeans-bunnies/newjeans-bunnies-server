package newjeans.bunnies.newjeansbunnies.domain.comment.repository

import newjeans.bunnies.newjeansbunnies.domain.comment.CommentEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.*

interface CommentRepository : CrudRepository<CommentEntity, String> {

    fun findByPostId(postId: String): List<CommentEntity>

    @Query("SELECT comment FROM CommentEntity comment WHERE comment.postId = :postId AND comment.state = true")
    fun findSliceBy(pageable: Pageable, postId: String): Optional<List<CommentEntity>>
}