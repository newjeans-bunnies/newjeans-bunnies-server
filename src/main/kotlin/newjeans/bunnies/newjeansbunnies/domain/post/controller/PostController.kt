package newjeans.bunnies.newjeansbunnies.domain.post.controller


import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.request.PostRequestDto
import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response.GetPostResponseDto
import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response.PostResponseDto
import newjeans.bunnies.newjeansbunnies.domain.post.service.CreatePostService
import newjeans.bunnies.newjeansbunnies.domain.post.service.GetPostService

import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/post")
@Configuration
class PostController(
    private val createPostService: CreatePostService,
    private val getPostService: GetPostService
) {
    @PostMapping("")
    fun makePost(
        @RequestBody postRequestDto: PostRequestDto
    ): PostResponseDto {
        return createPostService.execute(postRequestDto)
    }

    @GetMapping("")
    fun getPost(
        @RequestParam("date") date: String
    ): List<GetPostResponseDto> {
        return getPostService.execute(date)
    }
}