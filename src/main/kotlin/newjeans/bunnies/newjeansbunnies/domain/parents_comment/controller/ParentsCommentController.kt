package newjeans.bunnies.newjeansbunnies.domain.parents_comment.controller


import jakarta.validation.Valid

import newjeans.bunnies.newjeansbunnies.domain.parents_comment.controller.dto.request.ParentsCommentRequestDto
import newjeans.bunnies.newjeansbunnies.domain.parents_comment.controller.dto.response.ParentsCommentBasicInfoGetResponseDto
import newjeans.bunnies.newjeansbunnies.domain.parents_comment.controller.dto.response.ParentsCommentDetailGetResponseDto
import newjeans.bunnies.newjeansbunnies.domain.parents_comment.controller.dto.response.ParentsCommentSendResponseDto
import newjeans.bunnies.newjeansbunnies.domain.parents_comment.error.exception.CommentIdBlankException
import newjeans.bunnies.newjeansbunnies.domain.parents_comment.error.exception.PostIdBlankException
import newjeans.bunnies.newjeansbunnies.domain.parents_comment.service.DeleteParentsCommentService
import newjeans.bunnies.newjeansbunnies.domain.parents_comment.service.ParentsCommentService
import newjeans.bunnies.newjeansbunnies.global.response.StatusResponseDto

import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.*


@RestController
@Configuration
@RequestMapping("/api/comment/parents")
class ParentsCommentController(
    private val parentsCommentService: ParentsCommentService,
    private val deleteParentsCommentService: DeleteParentsCommentService
) {
    @PostMapping("/send")
    fun sendComment(
        @RequestBody @Valid parentsCommentRequestDto: ParentsCommentRequestDto
    ): ParentsCommentSendResponseDto {
        return parentsCommentService.send(parentsCommentRequestDto)
    }

    @GetMapping("/basic-info")
    fun getCommentBasicInfo(
        @RequestParam("postId") postId: String
    ): List<ParentsCommentBasicInfoGetResponseDto> {
        if (postId.isBlank())
            throw PostIdBlankException
        return parentsCommentService.getBasicInfo(postId)
    }

    @GetMapping("/detail")
    fun getCommentDetail(
        @RequestParam userId: String,
        @RequestParam("postId") postId: String,
    ): List<ParentsCommentDetailGetResponseDto> {
        if (postId.isBlank())
            throw PostIdBlankException
        return parentsCommentService.getDetail(postId, userId)
    }


    @DeleteMapping
    fun deleteComment(
        @RequestParam("parentsCommentId") parentsCommentId: String
    ): StatusResponseDto {
        if (parentsCommentId.isBlank())
            throw CommentIdBlankException
        return deleteParentsCommentService.deleteParentsCommentByParentsCommentId(parentsCommentId)
    }
}