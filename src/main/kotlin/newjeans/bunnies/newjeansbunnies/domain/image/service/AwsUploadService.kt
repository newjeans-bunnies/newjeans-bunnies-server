package newjeans.bunnies.newjeansbunnies.domain.image.service

import com.amazonaws.HttpMethod
import com.amazonaws.services.s3.model.*
import newjeans.bunnies.newjeansbunnies.domain.image.controller.dto.request.FinishUploadRequest
import newjeans.bunnies.newjeansbunnies.domain.image.controller.dto.request.PreSignedUploadInitiateRequest
import newjeans.bunnies.newjeansbunnies.domain.image.controller.dto.request.PreSignedUrlAbortRequest
import newjeans.bunnies.newjeansbunnies.domain.image.controller.dto.request.PreSignedUrlCreateRequest
import newjeans.bunnies.newjeansbunnies.domain.image.controller.dto.response.CreatePresignedUrlResponse
import newjeans.bunnies.newjeansbunnies.global.config.AwsS3Config
import newjeans.bunnies.newjeansbunnies.global.response.StatusResponseDto
import newjeans.bunnies.newjeansbunnies.global.utils.CheckFileExtension
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import java.net.URLConnection
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Service
@Configuration
class AwsUploadService(
    private val awsS3Config: AwsS3Config,
    @Value("\${cloud.aws.s3.bucket}")
    private val bucket: String,
    private val checkFileExtension: CheckFileExtension
) {

    fun initiateUpload(request: PreSignedUploadInitiateRequest): InitiateMultipartUploadResult {
        checkFileExtension.execute(request.fileType)
        val objectMetadata = ObjectMetadata()
        objectMetadata.contentLength = request.fileSize
        objectMetadata.contentType = URLConnection.guessContentTypeFromName(request.fileType)
        return awsS3Config.amazonS3Client().initiateMultipartUpload(
            InitiateMultipartUploadRequest(bucket, "post-image/", objectMetadata)
        )
    }

    fun createPresignedUrl(request: PreSignedUrlCreateRequest): CreatePresignedUrlResponse {
        val expirationTime = Date.from(
            LocalDateTime.now().plusMinutes(10).atZone(ZoneId.systemDefault()).toInstant()
        )
        val generatePresignedUrlRequest =
            GeneratePresignedUrlRequest(bucket, "post-image/")
                .withMethod(HttpMethod.PUT)
                .withExpiration(expirationTime) // presigned url 만료 시간 설정
        generatePresignedUrlRequest.addRequestParameter("uploadId", request.uploadId)
        generatePresignedUrlRequest.addRequestParameter("partNumber", request.partNumber.toString())
        return CreatePresignedUrlResponse(awsS3Config.amazonS3Client().generatePresignedUrl(generatePresignedUrlRequest))
    }

    fun completeUpload(finishUploadRequest: FinishUploadRequest): CompleteMultipartUploadResult {
        val partETags = finishUploadRequest.parts.map { PartETag(it.partNumber, it.eTag) }
        val completeMultipartUploadRequest = CompleteMultipartUploadRequest(
            bucket,
            "post-image/",
            finishUploadRequest.uploadId,
            partETags,
        )

        return awsS3Config.amazonS3Client().completeMultipartUpload(completeMultipartUploadRequest)
    }

    fun abortMultipartUpload(request: PreSignedUrlAbortRequest) : StatusResponseDto {
        val abortMultipartUploadRequest =
            AbortMultipartUploadRequest(bucket, "post-image/", request.uploadId)
        awsS3Config.amazonS3Client().abortMultipartUpload(abortMultipartUploadRequest)
        return StatusResponseDto(status = 204,  message = "이미지가 삭제됨")
    }
}