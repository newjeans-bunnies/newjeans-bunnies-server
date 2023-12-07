package newjeans.bunnies.newjeansbunnies.domain.comment.controller


import newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.request.ParentsCommentRequestDto
import newjeans.bunnies.newjeansbunnies.domain.comment.service.ParentsCommentService

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.context.annotation.Configuration


@RestController
@Configuration
@RequestMapping("/api/comment/parents")
class ParentsCommentController(
    private val parentsCommentService: ParentsCommentService
) {
    @PostMapping("")
    fun sendComment(
        @RequestBody parentsCommentRequestDto: ParentsCommentRequestDto
    ) {
        parentsCommentService.send(parentsCommentRequestDto)
    }

    @DeleteMapping("")
    fun deleteComment(
        @RequestParam("uuid") id: String
    ) {
        parentsCommentService.delete(id)
    }
}