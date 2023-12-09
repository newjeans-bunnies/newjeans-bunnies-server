package newjeans.bunnies.newjeansbunnies.domain.comment.repository


import newjeans.bunnies.newjeansbunnies.domain.comment.ChildrenCommentEntity

import org.springframework.data.repository.CrudRepository

import java.util.*


interface ChildrenCommentRepository: CrudRepository<ChildrenCommentEntity, String> {
    fun findByParentCommentId(parentCommentId: String): Optional<List<ChildrenCommentEntity>>
}