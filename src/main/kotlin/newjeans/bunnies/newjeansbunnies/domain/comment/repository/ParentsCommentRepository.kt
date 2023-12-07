package newjeans.bunnies.newjeansbunnies.domain.comment.repository


import newjeans.bunnies.newjeansbunnies.domain.comment.ParentsCommentEntity
import org.springframework.data.repository.CrudRepository


interface ParentsCommentRepository : CrudRepository<ParentsCommentEntity, String> {
}