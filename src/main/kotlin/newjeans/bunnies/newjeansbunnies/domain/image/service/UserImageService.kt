package newjeans.bunnies.newjeansbunnies.domain.image.service


import com.amazonaws.HttpMethod
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.Headers
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
import newjeans.bunnies.newjeansbunnies.domain.image.controller.dto.response.UserImagePreSignedUrlResponseDto
import newjeans.bunnies.newjeansbunnies.domain.image.error.exception.ImageExtensionNotForundException
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import java.util.*

@Service
@Configuration
class UserImageService(
    @Value("\${cloud.aws.s3.bucket}")
    private val bucket: String,
    private val amazonS3: AmazonS3,
) {
    fun getPreSignedUrl(imageName: String, id: String): UserImagePreSignedUrlResponseDto {

        val fileExtension = imageName.substringAfterLast('.', "")

        if (fileExtension.isBlank()) //파일 확장자 유무 확인
            throw ImageExtensionNotForundException

        val generatePresignedUrlRequest: GeneratePresignedUrlRequest =
            getGeneratePreSignedUrlRequest(bucket, "user-image/$id.webp")

        return UserImagePreSignedUrlResponseDto(
            amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString()
        )
    }

    private fun getGeneratePreSignedUrlRequest(bucket: String, fileName: String): GeneratePresignedUrlRequest {
        val generatePresignedUrlRequest = GeneratePresignedUrlRequest(bucket, fileName)
            .withMethod(HttpMethod.PUT)
            .withExpiration(getPreSignedUrlExpiration())
        generatePresignedUrlRequest.addRequestParameter(
            Headers.S3_CANNED_ACL,
            CannedAccessControlList.PublicRead.toString()
        )
        return generatePresignedUrlRequest
    }

    private fun getPreSignedUrlExpiration(): Date {
        val expiration = Date()
        var expTimeMillis: Long = expiration.time
        expTimeMillis += (1000 * 10 * 60).toLong()
        expiration.setTime(expTimeMillis)
        return expiration
    }
}