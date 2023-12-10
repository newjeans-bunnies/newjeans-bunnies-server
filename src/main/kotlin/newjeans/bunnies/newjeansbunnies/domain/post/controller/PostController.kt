package newjeans.bunnies.newjeansbunnies.domain.post.controller


import jakarta.validation.Valid

import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.request.PostRequestDto
import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response.GetPostBasicResponseDto
import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response.GetPostDetailResponseDto
import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response.PostResponseDto
import newjeans.bunnies.newjeansbunnies.domain.post.service.CreatePostService
import newjeans.bunnies.newjeansbunnies.domain.post.service.GetPostBasicService
import newjeans.bunnies.newjeansbunnies.domain.post.service.GetPostDetailService
import newjeans.bunnies.newjeansbunnies.domain.post.service.GetPostService

import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/post")
@Configuration
class PostController(
    private val createPostService: CreatePostService,
    private val getPostBasicInfoService: GetPostBasicService,
    private val getPostDetailService: GetPostDetailService,
    private val getPostService: GetPostService
) {
    @PostMapping
    fun makePost(
        @RequestBody @Valid postRequestDto: PostRequestDto
    ): PostResponseDto {
        return createPostService.execute(postRequestDto)
    }

    @GetMapping("/basic-info")
    fun getPostBasicInfoList(
        @RequestParam date: String
    ): List<GetPostBasicResponseDto> {
        return getPostBasicInfoService.execute(date)
    }

    @GetMapping("/detail")
    fun getPostDetailList(
        @RequestParam date: String,
        @RequestParam userId: String
        ): List<GetPostDetailResponseDto>{
        return getPostDetailService.execute(date, userId)
    }
    @GetMapping("/basic-info/{uuid}")
    fun getPostBasicInfo(
        @PathVariable("uuid") id: String
    ): GetPostBasicResponseDto {
        return getPostService.getPostBasicInfo(id)
    }

    @GetMapping("/detail/{uuid}")
    fun getPostDetail(
        @PathVariable("uuid") id: String,
        @RequestParam userId: String
    ): GetPostDetailResponseDto {
        return getPostService.getPostDetail(id, userId)
    }

}