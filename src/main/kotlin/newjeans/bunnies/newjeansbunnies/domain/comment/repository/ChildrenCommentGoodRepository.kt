package newjeans.bunnies.newjeansbunnies.domain.comment.repository


import newjeans.bunnies.newjeansbunnies.domain.comment.ChildrenCommentGoodEntity
import org.springframework.data.repository.CrudRepository


interface ChildrenCommentGoodRepository: CrudRepository<ChildrenCommentGoodEntity, Long> {

    fun existsByChildrenCommentIdAndUserId(childrenCommentId: String, userId: String): Boolean

    fun deleteByChildrenCommentIdAndUserId(childrenCommentId: String, userId: String)

    fun countByChildrenCommentId(childrenCommentId: String): Long

}