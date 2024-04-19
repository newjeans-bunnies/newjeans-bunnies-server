package newjeans.bunnies.newjeansbunnies.domain.image.controller

import newjeans.bunnies.newjeansbunnies.domain.image.controller.dto.request.PreSignedUrlCreateRequest
import newjeans.bunnies.newjeansbunnies.domain.image.controller.dto.response.CreatePreSignedUrlResponse
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

    //Multipart 업로드 시작
//    @PostMapping("/initiate-upload")
//    fun initiateUpload(
//        @RequestBody request: PreSignedUploadInitiateRequest
//    ): InitiateMultipartUploadResult {
//        return awsUploadService.initiateUpload(request)
//    }

    // PreSignedURL 발급
    @PostMapping("/presigned-url")
    fun issuePreSignedUrl(
        @RequestBody request: List<PreSignedUrlCreateRequest>,
        @RequestParam("post-id") postId: String
    ): List<CreatePreSignedUrlResponse> {
        return awsUploadService.createPreSignedUrl(request, postId)
    }

    // Multipart 업로드 완료
    @PostMapping("/complete-upload")
    fun completeUpload(
        @RequestParam("image-id") imageId: String
    ): StatusResponseDto {
        return awsUploadService.completeUpload(imageId)
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