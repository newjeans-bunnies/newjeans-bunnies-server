package newjeans.bunnies.newjeansbunnies.domain.post.controller

import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.request.FixPostRequestDto
import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.request.PostRequestDto
import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response.PostDto
import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response.PostGoodResponseDto
import newjeans.bunnies.newjeansbunnies.domain.post.service.PostGoodService
import newjeans.bunnies.newjeansbunnies.domain.post.service.PostService
import newjeans.bunnies.newjeansbunnies.global.response.StatusResponseDto
import newjeans.bunnies.newjeansbunnies.global.security.principle.CustomUserDetails
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Slice
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/post")
@Configuration
class PostController(
    private val postGoodService: PostGoodService,
    private val postService: PostService,
) {
    @PostMapping("/create")
    fun createPost(
        @RequestBody postRequestDto: PostRequestDto,
        @AuthenticationPrincipal auth: CustomUserDetails,
    ): StatusResponseDto {
        return postService.createPost(postRequestDto, auth.username)
    }

    @GetMapping
    fun getPost(
        @AuthenticationPrincipal auth: CustomUserDetails?,
        @RequestParam size: Int,
        @RequestParam page: Int,
    ): Slice<PostDto> {
        return postService.getPost(size, page, auth?.username)
    }

    @GetMapping("/{userId}")
    fun getUserPost(
        @RequestParam size: Int,
        @RequestParam page: Int,
        @PathVariable userId: String,
        @AuthenticationPrincipal auth: CustomUserDetails?,
    ): Slice<PostDto> {
        return postService.getUserPost(size, page, userId, auth?.username)
    }

    @PostMapping("/good")
    fun goodPost(
        @RequestParam("post-id") postId: String,
        @RequestParam("user-id") userId: String,
        @AuthenticationPrincipal auth: CustomUserDetails?,
    ): PostGoodResponseDto {
        return postGoodService.postGood(postId, userId, auth?.username)
    }

    @PatchMapping("/fix")
    fun fixPost(
        @RequestBody fixPostRequestDto: FixPostRequestDto,
        @RequestParam("post-id") postId: String,
        @AuthenticationPrincipal auth: CustomUserDetails,
    ): StatusResponseDto {
        return postService.fixPost(fixPostRequestDto, auth.username, postId)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/delete")
    fun deletePost(
        @RequestParam("post-id") postId: String,
        @AuthenticationPrincipal auth: CustomUserDetails,
    ): StatusResponseDto {
        return postService.disabledPost(postId, auth.username)
    }

}