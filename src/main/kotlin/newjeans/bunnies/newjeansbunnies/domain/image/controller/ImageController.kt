package newjeans.bunnies.newjeansbunnies.domain.image.controller

import newjeans.bunnies.newjeansbunnies.domain.image.controller.dto.request.CreateImageRequestDto
import newjeans.bunnies.newjeansbunnies.domain.image.controller.dto.response.ImageResponseDto
import newjeans.bunnies.newjeansbunnies.domain.image.service.AwsUploadService
import newjeans.bunnies.newjeansbunnies.domain.image.service.ImageService
import newjeans.bunnies.newjeansbunnies.global.response.StatusResponseDto
import org.springframework.context.annotation.Configuration
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
        @RequestParam date: String
    ): List<ImageResponseDto> {
        return imageService.getListImage(date)
    }

    // 사진 삭제
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping
    fun deleteImage(
        @RequestParam imageId: String
    ): StatusResponseDto {
        return imageService.disabledImage(imageId)
    }


    @PostMapping
    fun createImage(
        @RequestBody createImageRequestDto: CreateImageRequestDto
    ) {
        imageService.createImage(createImageRequestDto)
    }

    @PostMapping("/complete-upload")
    fun completeUpload(
        @RequestParam("post-id") postId: String
    ): StatusResponseDto {
        return awsUploadService.completeUpload(postId)
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