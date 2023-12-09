package newjeans.bunnies.newjeansbunnies.domain.comment.controller


import jakarta.validation.Valid
import newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.request.ParentsCommentRequestDto
import newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.response.ParentsCommentGetResponseDto
import newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.response.ParentsCommentSendResponseDto
import newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.response.StatusResponseDto
import newjeans.bunnies.newjeansbunnies.domain.comment.error.exception.CommentIdBlankException
import newjeans.bunnies.newjeansbunnies.domain.comment.error.exception.PostIdBlankException
import newjeans.bunnies.newjeansbunnies.domain.comment.service.ParentsCommentService
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.*


@RestController
@Configuration
@RequestMapping("/api/comment/parents")
class ParentsCommentController(
    private val parentsCommentService: ParentsCommentService
) {
    @PostMapping
    fun sendComment(
        @RequestBody @Valid parentsCommentRequestDto: ParentsCommentRequestDto
    ): ParentsCommentSendResponseDto {
        return parentsCommentService.send(parentsCommentRequestDto)
    }

    @GetMapping
    fun getComment(
        @RequestParam("postId") postId: String
    ): List<ParentsCommentGetResponseDto>{
        if(postId.isBlank())
            throw PostIdBlankException
        return parentsCommentService.get(postId)
    }

    @DeleteMapping
    fun deleteComment(
        @RequestParam("parentsCommentId") parentsCommentId: String
    ): StatusResponseDto {
        if(parentsCommentId.isBlank())
            throw CommentIdBlankException
        return parentsCommentService.delete(parentsCommentId)
    }
}