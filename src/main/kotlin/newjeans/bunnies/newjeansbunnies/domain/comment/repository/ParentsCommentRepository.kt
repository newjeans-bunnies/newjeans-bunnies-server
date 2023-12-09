package newjeans.bunnies.newjeansbunnies.domain.comment.repository


import newjeans.bunnies.newjeansbunnies.domain.comment.ParentsCommentEntity
import org.springframework.data.repository.CrudRepository
import java.util.*


interface ParentsCommentRepository : CrudRepository<ParentsCommentEntity, String> {
    fun findByPostId(postId: String): Optional<List<ParentsCommentEntity>>
}