package newjeans.bunnies.newjeansbunnies.domain.image.controller

import newjeans.bunnies.newjeansbunnies.domain.image.controller.dto.request.CreateImageRequestDto
import newjeans.bunnies.newjeansbunnies.domain.image.controller.dto.response.ImageResponseDto
import newjeans.bunnies.newjeansbunnies.domain.image.service.AwsUploadService
import newjeans.bunnies.newjeansbunnies.domain.image.service.ImageService
import newjeans.bunnies.newjeansbunnies.global.response.StatusResponseDto
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Slice
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/image")
@Configuration
class ImageController(
    private val imageService: ImageService, private val awsUploadService: AwsUploadService
) {

    //사진 리스트로 가져오기
    @GetMapping
    fun getImages(
        @RequestParam size: Int,
        @RequestParam page: Int,
    ): Slice<ImageResponseDto> {
        return imageService.getListImage(size, page)
    }

    // 사진 삭제
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping
    fun deleteImage(
        @RequestParam(value = "image-id") imageId: String
    ): StatusResponseDto {
        return imageService.disabledImage(imageId)
    }

    @PostMapping
    fun createImage(
        @RequestBody createImageRequestDto: List<CreateImageRequestDto>,
        @RequestParam("user-id") userId: String,
        @RequestParam("post-id") postId: String,
    ) {
        imageService.createImage(createImageRequestDto, userId, postId)
    }

    // S3에 업로드된 사진 삭제
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/abort-upload")
    fun abortUpload(
        @RequestParam("image-id") imageId: String
    ): StatusResponseDto {
        return awsUploadService.deleteMultipartUpload(imageId)
    }
}