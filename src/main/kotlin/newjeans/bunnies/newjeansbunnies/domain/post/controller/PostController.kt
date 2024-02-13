package newjeans.bunnies.newjeansbunnies.domain.post.controller


import jakarta.validation.Valid
import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.request.PostRequestDto
import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response.GetPostBasicResponseDto
import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response.GetPostDetailResponseDto
import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response.PostImageResponseDto
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
    private val getPostImageService: GetPostImageService
) {
    @PostMapping
    fun makePost(
        @RequestPart("uploadFiles") multipartFiles: List<MultipartFile>,
        @ModelAttribute @Valid postRequestDto: PostRequestDto
    ): PostResponseDto {
        return createPostService.execute(postRequestDto, multipartFiles)
    }

    @GetMapping("/image")
    fun getPostImage(
        @RequestParam("post-id") postId: String
    ): List<PostImageResponseDto>{
        return getPostImageService.execute(postId)
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
    ): List<GetPostDetailResponseDto> {
        return getPostDetailService.execute(date, userId)
    }

    @GetMapping("/user/detail/{userId}")
    fun getUserPostDetailList(
        @PathVariable userId: String,
        @RequestParam date: String
    ): List<GetPostDetailResponseDto> {
        return getUserPostDetailService.execute(date, userId)
    }

    @GetMapping("/user/basic-info/{userId}")
    fun getUserPostBasicInfoList(
        @PathVariable userId: String,
        @RequestParam date: String
    ): List<GetPostBasicResponseDto> {
        return getUserPostBasicInfoService.execute(userId, date)
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

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/delete")
    fun deletePost(
        @RequestParam("postid") postId: String
    ): StatusResponseDto {
        return deletePostService.deletePostByPostId(postId)
    }
}