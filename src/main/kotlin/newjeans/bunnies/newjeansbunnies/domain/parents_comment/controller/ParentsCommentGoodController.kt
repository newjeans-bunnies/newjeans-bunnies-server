package newjeans.bunnies.newjeansbunnies.domain.parents_comment.controller


import newjeans.bunnies.newjeansbunnies.domain.parents_comment.controller.dto.response.ParentsCommentGoodResponseDto
import newjeans.bunnies.newjeansbunnies.domain.parents_comment.service.ParentsCommentGoodService

import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@Configuration
@RequestMapping("/api/comment/parents/good")
class ParentsCommentGoodController(
    private val parentsCommentGoodService: ParentsCommentGoodService
) {
    @PostMapping
    fun goodPost(
        @RequestParam("comment-id") commentId: String,
        @RequestParam("user-id") userId: String
    ): ParentsCommentGoodResponseDto {
        return parentsCommentGoodService.execute(commentId,userId)
    }
}