package newjeans.bunnies.newjeansbunnies.domain.comment.repository

import newjeans.bunnies.newjeansbunnies.domain.comment.CommentEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.*

interface CommentRepository : CrudRepository<CommentEntity, String> {

    @Query("SELECT comment FROM CommentEntity comment WHERE comment.postId = :postId AND comment.state = true")
    fun findSliceBy(pageable: Pageable, postId: String): Optional<Slice<CommentEntity>>


    fun findByPostId(postId: String): Optional<List<CommentEntity>>
}