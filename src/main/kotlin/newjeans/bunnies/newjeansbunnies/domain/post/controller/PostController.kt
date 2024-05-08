package newjeans.bunnies.newjeansbunnies.domain.post.controller

import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.request.PostRequestDto
import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response.CreatePostResponseDto
import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response.PostDto
import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response.PostGoodResponseDto
import newjeans.bunnies.newjeansbunnies.domain.post.service.PostGoodService
import newjeans.bunnies.newjeansbunnies.domain.post.service.PostService
import newjeans.bunnies.newjeansbunnies.domain.post.service.UserPostService
import newjeans.bunnies.newjeansbunnies.global.response.StatusResponseDto
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/post")
@Configuration
class PostController(
    private val postGoodService: PostGoodService,
    private val postService: PostService,
    private val userPostService: UserPostService
) {
    @PostMapping
    fun createPost(
        @RequestBody postRequestDto: PostRequestDto
    ): CreatePostResponseDto {
        return postService.createPost(postRequestDto)
    }

    @GetMapping
    fun getPost(
        @RequestParam("user-id") userId: String,
        @RequestParam size: Int,
        @RequestParam page: Int,
    ): List<PostDto> {
        return postService.getPost(size, page, userId)
    }

    @PostMapping("/good")
    fun goodPost(
        @RequestParam("post-id") postId: String, @RequestParam("user-id") userId: String
    ): PostGoodResponseDto {
        return postGoodService.execute(postId, userId)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping
    fun deletePost(
        @RequestParam("post-id") postId: String
    ): StatusResponseDto {
        return postService.deletePost(postId)
    }

}