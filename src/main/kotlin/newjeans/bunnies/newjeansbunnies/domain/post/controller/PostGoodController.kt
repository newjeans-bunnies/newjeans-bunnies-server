package newjeans.bunnies.newjeansbunnies.domain.post.controller


import newjeans.bunnies.newjeansbunnies.domain.post.service.PostGoodService

import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/post/good")
@Configuration
class PostGoodController(
    private val postGoodService: PostGoodService
) {
    @GetMapping("")
    fun goodPost(
        @RequestParam("post-id") postId: String,
        @RequestParam("user-id") userId: String
    ) {
        postGoodService.execute(postId, userId)
    }
}