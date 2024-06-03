package newjeans.bunnies.newjeansbunnies.domain.comment.controller

import newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.request.CommentRequestDto
import newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.request.FixCommentRequestDto
import newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.response.CommentGoodResponseDto
import newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.response.CommentResponseDto
import newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.response.CreateCommentResponseDto
import newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.response.FixCommentResponseDto
import newjeans.bunnies.newjeansbunnies.domain.comment.service.CommentGoodService
import newjeans.bunnies.newjeansbunnies.domain.comment.service.CommentService
import newjeans.bunnies.newjeansbunnies.global.response.StatusResponseDto
import newjeans.bunnies.newjeansbunnies.global.security.principle.CustomUserDetails
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Slice
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

// authorizedUser

@RestController
@RequestMapping("api/comment")
@Configuration
class CommentController(
    private val commentService: CommentService,
    private val commentGoodService: CommentGoodService,
) {

    //authorizedUser
    @PostMapping("/good")
    fun goodComment(
        @RequestParam("comment-id") commentId: String,
        @AuthenticationPrincipal auth: CustomUserDetails?,
    ): CommentGoodResponseDto {
        return commentGoodService.goodComment(commentId, auth?.username)
    }

    @PostMapping
    fun createComment(
        @RequestBody commentRequestDto: CommentRequestDto,
        @AuthenticationPrincipal auth: CustomUserDetails?
    ): CreateCommentResponseDto {
        return commentService.createComment(commentRequestDto, auth?.username)
    }

    @GetMapping
    fun getComment(
        @RequestParam page: Int,
        @RequestParam size: Int,
        @RequestParam("post-id") postId: String,
        @AuthenticationPrincipal auth: CustomUserDetails?
    ): Slice<CommentResponseDto> {
        return commentService.getComment(postId, size, page, auth?.username)
    }

    @PatchMapping("/fix")
    fun fixComment(
        @RequestBody fixCommentRequestDto: FixCommentRequestDto,
        @RequestParam("comment-id") commentId: String,
        @AuthenticationPrincipal auth: CustomUserDetails?,
    ): FixCommentResponseDto {
        return commentService.fixComment(commentId, fixCommentRequestDto, auth?.username)
    }

    @PatchMapping("/delete")
    fun deleteComment(
        @RequestParam("comment-id") commentId: String,
        @AuthenticationPrincipal auth: CustomUserDetails?
    ): StatusResponseDto {
        return commentService.disabledComment(commentId, auth?.username)
    }
}