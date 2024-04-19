package newjeans.bunnies.newjeansbunnies.domain.image.service

import com.amazonaws.HttpMethod
import com.amazonaws.services.s3.Headers
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult
import com.amazonaws.services.s3.model.ObjectMetadata
import newjeans.bunnies.newjeansbunnies.domain.image.Constant.preSignedUrlExpirationTime
import newjeans.bunnies.newjeansbunnies.domain.image.controller.dto.request.PreSignedUploadInitiateRequest
import newjeans.bunnies.newjeansbunnies.domain.image.controller.dto.request.PreSignedUrlCreateRequest
import newjeans.bunnies.newjeansbunnies.domain.image.controller.dto.response.CreatePreSignedUrlResponse
import newjeans.bunnies.newjeansbunnies.global.config.AwsS3Config
import newjeans.bunnies.newjeansbunnies.global.response.StatusResponseDto
import newjeans.bunnies.newjeansbunnies.global.utils.CheckFileExtension
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import java.net.URLConnection
import java.time.LocalDateTime

@Service
@Configuration
class AwsUploadService(
    private val awsS3Config: AwsS3Config,
    @Value("\${cloud.aws.s3.bucket}") private val bucket: String,
    @Value("\${cloud.aws.region.static}") private val location: String,
    private val checkFileExtension: CheckFileExtension,
    private val imageService: ImageService
) {

    fun initiateUpload(request: PreSignedUploadInitiateRequest): InitiateMultipartUploadResult {
        // 파일 확장자 확인
        checkFileExtension.execute(request.fileType)

        val objectMetadata = ObjectMetadata()
        objectMetadata.contentType = URLConnection.guessContentTypeFromName(request.fileType)

        return awsS3Config.amazonS3Client().initiateMultipartUpload(
            InitiateMultipartUploadRequest(bucket, "image/${request.fileName}")
        )
    }

    // PreSignedUrl 생성
    fun createPreSignedUrl(request: List<PreSignedUrlCreateRequest>,  postId: String): List<CreatePreSignedUrlResponse> {

        val generatePreSignedUrlRequests: MutableList<GeneratePresignedUrlRequest> = mutableListOf()

        // preSigned url 만료 시간
        val expirationTime = java.sql.Timestamp.valueOf(LocalDateTime.now().plusMinutes(preSignedUrlExpirationTime))

        request.map {
            // 파일 확장자 확인
            checkFileExtension.execute(it.fileType)

            val generatePreSignedUrlRequest =
                GeneratePresignedUrlRequest(bucket, "image/${it.fileName}").withMethod(HttpMethod.PUT)
                    .withExpiration(expirationTime)

            generatePreSignedUrlRequest.putCustomRequestHeader(Headers.CONTENT_TYPE, it.fileType)
            generatePreSignedUrlRequest.putCustomRequestHeader(Headers.CONTENT_LENGTH, it.fileSize)

            // PreSignedUrl 생성
            generatePreSignedUrlRequests.add(generatePreSignedUrlRequest)
        }

        for(index in request.indices)
        request.map {
            // 사진 생성
            imageService.createImage(
                imageId = it.fileName, postId = postId
            )
        }


        return generatePreSignedUrlRequests.mapIndexed { index, item ->
            CreatePreSignedUrlResponse(
                awsS3Config.amazonS3Client().generatePresignedUrl(item).toString(),
                request[index].fileName,
                postId
            )
        }
    }


    // 업로드 한 파일 확정
    fun completeUpload(imageId: String): StatusResponseDto {
        val createDate = LocalDateTime.now().toString()
        val imageURL = "https://$bucket.s3.$location.amazonaws.com/image/$imageId"
        imageService.activationImage(imageId, imageURL, createDate)
        return StatusResponseDto(200, "사진이 활성됨")
    }

    // 업로드 하려는 파일 삭제
//    fun abortMultipartUpload(request: PreSignedUrlAbortRequest): StatusResponseDto {
//        val abortMultipartUploadRequest = AbortMultipartUploadRequest(bucket, "post-image/", request.uploadId)
//        awsS3Config.amazonS3Client().abortMultipartUpload(abortMultipartUploadRequest)
//        return StatusResponseDto(status = 204, message = "이미지가 삭제됨")
//    }

    // 비활성화되어 있는 사진 삭제 하기
    fun deleteMultipartUpload(imageId: String): StatusResponseDto {

        // 사진 아이디를 사용해 사진 삭제
        imageService.deleteImage(imageId)

        return StatusResponseDto(204, "사진이 삭제됨")
    }

}
