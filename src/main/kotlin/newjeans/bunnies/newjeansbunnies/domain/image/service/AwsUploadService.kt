package newjeans.bunnies.newjeansbunnies.domain.image.service

import com.amazonaws.HttpMethod
import com.amazonaws.services.s3.model.*
import newjeans.bunnies.newjeansbunnies.domain.image.controller.dto.request.FinishUploadRequest
import newjeans.bunnies.newjeansbunnies.domain.image.controller.dto.request.PreSignedUploadInitiateRequest
import newjeans.bunnies.newjeansbunnies.domain.image.controller.dto.request.PreSignedUrlAbortRequest
import newjeans.bunnies.newjeansbunnies.domain.image.controller.dto.request.PreSignedUrlCreateRequest
import newjeans.bunnies.newjeansbunnies.domain.image.controller.dto.response.CreatePreSignedUrlResponse
import newjeans.bunnies.newjeansbunnies.domain.image.error.exception.NotExistZoneIdException
import newjeans.bunnies.newjeansbunnies.global.config.AwsS3Config
import newjeans.bunnies.newjeansbunnies.global.response.StatusResponseDto
import newjeans.bunnies.newjeansbunnies.global.utils.CheckFileExtension
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId

@Service
@Configuration
class AwsUploadService(
    private val awsS3Config: AwsS3Config,
    @Value("\${cloud.aws.s3.bucket}") private val bucket: String,
    private val checkFileExtension: CheckFileExtension,
    private val imageService: ImageService
) {

    fun initiateUpload(request: PreSignedUploadInitiateRequest): InitiateMultipartUploadResult {
        // 파일 확장자 확인
        checkFileExtension.execute(request.fileType)

        return awsS3Config.amazonS3Client().initiateMultipartUpload(
            InitiateMultipartUploadRequest(bucket, "image/${request.fileName}${request.fileType}")
        )
    }

    // PreSignedUrl 생성
    fun createPreSignedUrl(request: PreSignedUrlCreateRequest): CreatePreSignedUrlResponse {

        // 파일 확장자 확인
        checkFileExtension.execute(request.fileType)

        val zoneId: ZoneId

        try {
            zoneId = ZoneId.of(request.zoneId)
        } catch (e: Exception) {
            throw NotExistZoneIdException
        }

        // preSigned url 만료 시간
        val expirationTime = java.sql.Timestamp.valueOf(LocalDateTime.now(zoneId).plusMinutes(3))

        val generatePreSignedUrlRequest = GeneratePresignedUrlRequest(
            bucket, "image/${request.fileName}${request.fileType}"
        ).withMethod(HttpMethod.PUT).withExpiration(expirationTime) // preSigned url 만료 시간 설정

        // Parameter 추가
        generatePreSignedUrlRequest.addRequestParameter("uploadId", request.uploadId)
        generatePreSignedUrlRequest.addRequestParameter("partNumber", request.partNumber.toString())

        return CreatePreSignedUrlResponse(
            awsS3Config.amazonS3Client().generatePresignedUrl(generatePreSignedUrlRequest),
        )
    }

    // 업로드 한 파일 확정
    fun completeUpload(finishUploadRequest: FinishUploadRequest): CompleteMultipartUploadResult {

        // 사진 생성 시간
        val createImageDate = LocalDateTime.now()

        val partETags = finishUploadRequest.parts.map { PartETag(it.partNumber, it.eTag) }

        val completeMultipartUploadRequest = CompleteMultipartUploadRequest(
            bucket,
            "image/${finishUploadRequest.fileFullName}",
            finishUploadRequest.uploadId,
            partETags,
        )

        val completeMultipartUploadResult =
            awsS3Config.amazonS3Client().completeMultipartUpload(completeMultipartUploadRequest)

        // 사진 생성
        imageService.createImage(
            createImageDate.toString(),
            finishUploadRequest.fileFullName,
            completeMultipartUploadResult.location,
            finishUploadRequest.postId
        )

        return completeMultipartUploadResult
    }

    // 업로드 하려는 파일 삭제
    fun abortMultipartUpload(request: PreSignedUrlAbortRequest): StatusResponseDto {
        val abortMultipartUploadRequest = AbortMultipartUploadRequest(bucket, "post-image/", request.uploadId)
        awsS3Config.amazonS3Client().abortMultipartUpload(abortMultipartUploadRequest)
        return StatusResponseDto(status = 204, message = "이미지가 삭제됨")
    }
}