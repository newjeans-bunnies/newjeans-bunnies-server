package newjeans.bunnies.newjeansbunnies.domain.image.controller

import com.amazonaws.services.s3.model.CompleteMultipartUploadResult
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult
import newjeans.bunnies.newjeansbunnies.domain.image.controller.dto.request.FinishUploadRequest
import newjeans.bunnies.newjeansbunnies.domain.image.controller.dto.request.PreSignedUploadInitiateRequest
import newjeans.bunnies.newjeansbunnies.domain.image.controller.dto.request.PreSignedUrlAbortRequest
import newjeans.bunnies.newjeansbunnies.domain.image.controller.dto.request.PreSignedUrlCreateRequest
import newjeans.bunnies.newjeansbunnies.domain.image.controller.dto.response.CreatePresignedUrlResponse
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
    @PostMapping("/list")
    fun getListImage(
        @RequestParam date: String
    ): List<ImageResponseDto> {
        return imageService.getListImage(date)
    }

    //Multipart 업로드 시작
    @PostMapping("/initiate-upload")
    fun initiateUpload(
        @RequestBody request: PreSignedUploadInitiateRequest
    ): InitiateMultipartUploadResult {
        return awsUploadService.initiateUpload(request)
    }

    // PreSignedURL 발급
    @PostMapping("/presigned-url")
    fun initiateUpload(
        @RequestBody request: PreSignedUrlCreateRequest,
    ): CreatePresignedUrlResponse {
        return awsUploadService.createPreSignedUrl(request)
    }

    // Multipart 업로드 완료
    @PostMapping("/complete-upload")
    fun initiateUpload3(
        @RequestBody finishUploadRequest: FinishUploadRequest,
    ): CompleteMultipartUploadResult {
        return awsUploadService.completeUpload(finishUploadRequest)
    }

    //Multipart 업로드 취소
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/abort-upload")
    fun initiateUpload3(
        @RequestBody request: PreSignedUrlAbortRequest,
    ): StatusResponseDto {
        return awsUploadService.abortMultipartUpload(request)
    }

}