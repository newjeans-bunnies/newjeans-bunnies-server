package newjeans.bunnies.newjeansbunnies.domain.post.controller

import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.request.PostRequestDto
import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response.PostResponseDto
import newjeans.bunnies.newjeansbunnies.domain.post.service.PostService
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/post")
@Configuration
class PostController(
    private val postService: PostService,
) {
    @PostMapping("")
    fun makePost(
        @RequestBody postRequestDto: PostRequestDto
    ): PostResponseDto {
        return postService.makePost(postRequestDto)
    }

//    @GetMapping("")
//    fun getPost(
//        @RequestParam("date") date: String
//    ): List<PostResponseDto> {
//        return postService.getPost(date)
//    }
}