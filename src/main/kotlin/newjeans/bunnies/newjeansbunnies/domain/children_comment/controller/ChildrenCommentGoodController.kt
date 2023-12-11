package newjeans.bunnies.newjeansbunnies.domain.children_comment.controller


import newjeans.bunnies.newjeansbunnies.domain.children_comment.controller.dto.response.ChildrenCommentGoodResponseDto
import newjeans.bunnies.newjeansbunnies.domain.children_comment.service.ChildrenCommentGoodService

import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@Configuration
@RequestMapping("/api/comment/children/good")
class ChildrenCommentGoodController(
    private val childrenCommentGoodService: ChildrenCommentGoodService
) {
    @PostMapping
    fun goodPost(
        @RequestParam("comment-id") commentId: String,
        @RequestParam("user-id") userId: String
    ): ChildrenCommentGoodResponseDto {
        return childrenCommentGoodService.execute(commentId, userId)
    }
}