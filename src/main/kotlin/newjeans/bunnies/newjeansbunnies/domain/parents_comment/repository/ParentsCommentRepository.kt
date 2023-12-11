package newjeans.bunnies.newjeansbunnies.domain.parents_comment.repository


import newjeans.bunnies.newjeansbunnies.domain.parents_comment.ParentsCommentEntity
import org.springframework.data.repository.CrudRepository
import java.util.*


interface ParentsCommentRepository : CrudRepository<ParentsCommentEntity, String> {
    fun findByPostId(postId: String): Optional<List<ParentsCommentEntity>>

    fun deleteByPostId(postId: String)

    fun existsByUuid(parentsCommentId: String): Boolean

}