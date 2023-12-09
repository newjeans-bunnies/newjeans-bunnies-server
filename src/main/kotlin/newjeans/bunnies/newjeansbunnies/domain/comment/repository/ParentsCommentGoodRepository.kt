package newjeans.bunnies.newjeansbunnies.domain.comment.repository


import newjeans.bunnies.newjeansbunnies.domain.comment.ParentsCommentGoodEntity

import org.springframework.data.repository.CrudRepository


interface ParentsCommentGoodRepository: CrudRepository<ParentsCommentGoodEntity, Long> {
    fun existsByParentsCommentIdAndUserId(parentsCommentId: String, userId: String): Boolean

    fun deleteByParentsCommentIdAndUserId(parentsCommentId: String, userId: String)

    fun countByParentsCommentId(parentsCommentId: String): Long
}