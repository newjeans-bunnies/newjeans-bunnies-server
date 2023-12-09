package newjeans.bunnies.newjeansbunnies.domain.comment.controller


import jakarta.validation.Valid
import newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.request.ChildrenCommentRequestDto
import newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.response.ChildrenCommentGetResponseDto
import newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.response.ChildrenCommentSendResponseDto
import newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.response.StatusResponseDto
import newjeans.bunnies.newjeansbunnies.domain.comment.error.exception.CommentIdBlankException
import newjeans.bunnies.newjeansbunnies.domain.comment.service.ChildrenCommentService
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.*


@RestController
@Configuration
@RequestMapping("/api/comment/children")
class ChildrenCommentController(
    private val childrenCommentService: ChildrenCommentService
) {
    @PostMapping
    fun sendComment(
        @RequestBody @Valid childrenCommentRequestDto: ChildrenCommentRequestDto
    ): ChildrenCommentSendResponseDto {
        return childrenCommentService.send(childrenCommentRequestDto)
    }

    @GetMapping
    fun getComment(
        @RequestParam("parentsCommentId") parentsCommentId: String
    ): List<ChildrenCommentGetResponseDto> {
        if (parentsCommentId.isBlank())
            throw CommentIdBlankException
        return childrenCommentService.get(parentsCommentId)
    }

    @DeleteMapping
    fun deleteComment(
        @RequestParam("comment-uuid") id: String
    ): StatusResponseDto {
        if (id.isBlank())
            throw CommentIdBlankException
        return childrenCommentService.delete(id)
    }

}