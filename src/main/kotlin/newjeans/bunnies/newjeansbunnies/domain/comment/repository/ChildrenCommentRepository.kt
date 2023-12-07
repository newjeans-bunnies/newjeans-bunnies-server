package newjeans.bunnies.newjeansbunnies.domain.comment.repository


import newjeans.bunnies.newjeansbunnies.domain.comment.ChildrenCommentEntity
import org.springframework.data.repository.CrudRepository


interface ChildrenCommentRepository: CrudRepository<ChildrenCommentEntity, String> {

}