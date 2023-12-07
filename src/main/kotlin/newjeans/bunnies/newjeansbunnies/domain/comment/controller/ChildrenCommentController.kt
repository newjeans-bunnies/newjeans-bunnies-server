package newjeans.bunnies.newjeansbunnies.domain.comment.controller


import newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.request.ChildrenCommentRequestDto
import newjeans.bunnies.newjeansbunnies.domain.comment.service.ChildrenCommentService
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.*


@RestController
@Configuration
@RequestMapping("/api/comment/children")
class ChildrenCommentController(
    private val childrenCommentService: ChildrenCommentService
) {
    @PostMapping("")
    fun sendComment(
        @RequestHeader("Authorization") token: String,
        @RequestBody childrenCommentRequestDto: ChildrenCommentRequestDto
    ){
        childrenCommentService.send()
    }

    @DeleteMapping("")
    fun deleteComment(){
        childrenCommentService.delete()
    }


}