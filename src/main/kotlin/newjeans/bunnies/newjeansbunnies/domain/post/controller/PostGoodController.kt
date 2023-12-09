package newjeans.bunnies.newjeansbunnies.domain.post.controller


import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response.PostGoodResponseDto
import newjeans.bunnies.newjeansbunnies.domain.post.service.PostGoodService
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/post/good")
@Configuration
class PostGoodController(
    private val postGoodService: PostGoodService
) {
    @PostMapping("")
    fun goodPost(
        @RequestParam("post-id") postId: String,
        @RequestParam("user-id") userId: String
    ): PostGoodResponseDto {
        return postGoodService.execute(postId, userId)
    }
}