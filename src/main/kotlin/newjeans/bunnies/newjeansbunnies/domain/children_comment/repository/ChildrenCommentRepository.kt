package newjeans.bunnies.newjeansbunnies.domain.children_comment.repository


import newjeans.bunnies.newjeansbunnies.domain.children_comment.ChildrenCommentEntity

import org.springframework.data.repository.CrudRepository

import java.util.*


interface ChildrenCommentRepository : CrudRepository<ChildrenCommentEntity, String> {
    fun findByParentCommentId(parentCommentId: String): Optional<List<ChildrenCommentEntity>>

    fun deleteByParentCommentId(parentCommentId: String)

    fun existsByUuid(parentCommentId: String): Boolean

}