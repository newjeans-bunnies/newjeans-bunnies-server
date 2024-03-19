package newjeans.bunnies.newjeansbunnies.domain.post.controller


import jakarta.validation.Valid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.request.PostRequestDto
import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response.GetPostBasicResponseDto
import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response.GetPostDetailResponseDto
import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response.PostGoodResponseDto
import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response.PostResponseDto
import newjeans.bunnies.newjeansbunnies.domain.post.service.*
import newjeans.bunnies.newjeansbunnies.global.response.StatusResponseDto
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile


@RestController
@RequestMapping("/api/post")
@Configuration
class PostController(
    private val getUserPostBasicInfoService: GetUserPostBasicInfoService,
    private val getUserPostDetailService: GetUserPostDetailService,
    private val createPostService: CreatePostService,
    private val getPostBasicInfoService: GetPostBasicInfoService,
    private val getPostDetailService: GetPostDetailService,
    private val getPostService: GetPostService,
    private val deletePostService: DeletePostService,
    private val postGoodService: PostGoodService
) {
    @PostMapping
    fun makePost(
        @RequestPart("uploadFiles") multipartFiles: List<MultipartFile>,
        @ModelAttribute @Valid postRequestDto: PostRequestDto
    ): PostResponseDto {
        return runBlocking(Dispatchers.IO) { createPostService.execute(postRequestDto, multipartFiles) }
    }




    @GetMapping("/basic-info")
    fun getPostBasicInfoList(
        @RequestParam date: String
    ): List<GetPostBasicResponseDto> {
        return runBlocking(Dispatchers.IO) { getPostBasicInfoService.execute(date) }
    }

    @GetMapping("/detail")
    fun getPostDetailList(
        @RequestParam date: String, @RequestParam userId: String
    ): List<GetPostDetailResponseDto> {
        return runBlocking(Dispatchers.IO) { getPostDetailService.execute(date, userId) }
    }

    @GetMapping("/user/detail/{userId}")
    fun getUserPostDetailList(
        @PathVariable userId: String, @RequestParam date: String
    ): List<GetPostDetailResponseDto> {
        return runBlocking(Dispatchers.IO) { getUserPostDetailService.execute(date, userId) }
    }

    @GetMapping("/user/basic-info/{userId}")
    fun getUserPostBasicInfoList(
        @PathVariable userId: String, @RequestParam date: String
    ): List<GetPostBasicResponseDto> {
        return runBlocking(Dispatchers.IO) { getUserPostBasicInfoService.execute(userId, date) }
    }

    @GetMapping("/basic-info/{uuid}")
    fun getPostBasicInfo(
        @PathVariable("uuid") id: String
    ): GetPostBasicResponseDto {
        return runBlocking(Dispatchers.IO) { getPostService.getPostBasicInfo(id) }
    }

    @GetMapping("/detail/{uuid}")
    fun getPostDetail(
        @PathVariable("uuid") id: String, @RequestParam userId: String
    ): GetPostDetailResponseDto {
        return runBlocking(Dispatchers.IO) { getPostService.getPostDetail(id, userId) }
    }





    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/delete")
    fun deletePost(
        @RequestParam("post-id") postId: String
    ): StatusResponseDto {
        return runBlocking(Dispatchers.IO) { deletePostService.deletePost(postId) }
    }

    @PostMapping("/good")
    fun goodPost(
        @RequestParam("post-id") postId: String, @RequestParam("user-id") userId: String
    ): PostGoodResponseDto {
        return runBlocking(Dispatchers.IO) { postGoodService.execute(postId, userId) }
    }
}