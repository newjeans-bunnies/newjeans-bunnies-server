package newjeans.bunnies.newjeansbunnies.domain.comment.controller

import newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.request.CommentRequestDto
import newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.request.FixCommentRequestDto
import newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.response.CommentResponseDto
import newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.response.FixCommentResponseDto
import newjeans.bunnies.newjeansbunnies.domain.comment.service.CommentService
import newjeans.bunnies.newjeansbunnies.global.response.StatusResponseDto
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Slice
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/comment")
@Configuration
class CommentController(
    private val commentService: CommentService
) {

    @PostMapping
    fun createComment(@RequestBody commentRequestDto: CommentRequestDto): CommentResponseDto {
        return commentService.createComment(commentRequestDto)
    }

    @GetMapping
    fun getComment(
        @RequestParam page: Int, @RequestParam size: Int, @RequestParam("post-id") postId: String
    ): Slice<CommentResponseDto> {
        return commentService.getComment(postId, size, page)
    }

    @PatchMapping
    fun fixComment(
        @RequestBody fixCommentRequestDto: FixCommentRequestDto, @RequestParam("comment-id") commentId: String
    ): FixCommentResponseDto {
        return commentService.fixComment(commentId, fixCommentRequestDto)
    }

    @DeleteMapping
    fun deleteComment(@RequestParam commentId: String): StatusResponseDto {
        return commentService.deleteComment(commentId)
    }
}