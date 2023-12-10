package newjeans.bunnies.newjeansbunnies.domain.comment.controller


import jakarta.validation.Valid

import newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.request.ChildrenCommentRequestDto
import newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.response.ChildrenCommentBasicGetResponseDto
import newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.response.ChildrenCommentDetailGetResponseDto
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
    @PostMapping("/send")
    fun sendComment(
        @RequestBody @Valid childrenCommentRequestDto: ChildrenCommentRequestDto
    ): ChildrenCommentSendResponseDto {
        return childrenCommentService.send(childrenCommentRequestDto)
    }

    @GetMapping("/basic-info")
    fun getCommentBasicInfo(
        @RequestParam parentsCommentId: String
    ): List<ChildrenCommentBasicGetResponseDto> {
        if (parentsCommentId.isBlank())
            throw CommentIdBlankException
        return childrenCommentService.getBasicInfo(parentsCommentId)
    }

    @GetMapping("detail")
    fun getCommentDetail(
        @RequestParam userId: String,
        @RequestParam parentsCommentId: String
    ): List<ChildrenCommentDetailGetResponseDto> {
        return childrenCommentService.getDetail(parentsCommentId, userId)
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