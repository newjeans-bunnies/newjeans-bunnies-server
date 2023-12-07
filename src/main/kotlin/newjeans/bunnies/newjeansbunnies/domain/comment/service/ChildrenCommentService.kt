package newjeans.bunnies.newjeansbunnies.domain.comment.service


import newjeans.bunnies.newjeansbunnies.domain.comment.repository.ChildrenCommentRepository
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service


@Service
@Configuration
class ChildrenCommentService(
    private val childrenCommentRepository: ChildrenCommentRepository
) {

    fun send(){

    }

    fun delete(){

    }
}