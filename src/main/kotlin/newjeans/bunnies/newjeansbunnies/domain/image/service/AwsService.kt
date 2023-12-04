package newjeans.bunnies.newjeansbunnies.domain.image.service

import com.amazonaws.HttpMethod
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.Headers
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


@Service
@Configuration
class AwsService(
    @Value("\${cloud.aws.s3.bucket}")
    private val bucket: String,
    private val amazonS3: AmazonS3
) {
    fun getPreSignedUrl(fileName: String, imageType: String): String {
        val generatePresignedUrlRequest: GeneratePresignedUrlRequest =
            getGeneratePreSignedUrlRequest(bucket, imageType+getCurrentDateInFormat() + fileName)

        return amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString()
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
        expTimeMillis += (1000 * 60 * 1).toLong()
        expiration.setTime(expTimeMillis)
        return expiration
    }

    private fun getCurrentDateInFormat(): String {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd/")
        return currentDateTime.format(formatter)
    }

}