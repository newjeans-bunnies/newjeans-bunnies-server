package newjeans.bunnies.newjeansbunnies.domain.image.service

import com.amazonaws.HttpMethod
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.Headers
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
import newjeans.bunnies.newjeansbunnies.domain.image.PostImageEntity
import newjeans.bunnies.newjeansbunnies.domain.image.UserImageEntity
import newjeans.bunnies.newjeansbunnies.domain.image.dto.response.PreSignedUrlResponseDto
import newjeans.bunnies.newjeansbunnies.domain.image.repository.PostImageRepository
import newjeans.bunnies.newjeansbunnies.domain.image.repository.UserImageRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


@Service
@Configuration
class AwsService(
    @Value("\${cloud.aws.s3.bucket}")
    private val bucket: String,
    @Value("\${cloud.aws.region.static}")
    private val location: String,
    private val amazonS3: AmazonS3,
    private val postImageRepository: PostImageRepository,
    private val userImageRepository: UserImageRepository
) {
    fun getPreSignedUrl(fileName: String, imageType: String, id: String): PreSignedUrlResponseDto {
        val uniqueFileName = generateFileName(fileName)
        val generatePresignedUrlRequest: GeneratePresignedUrlRequest =
            getGeneratePreSignedUrlRequest(bucket, imageType + getCurrentDateInFormat() + uniqueFileName)

        if (imageType == "post-image/")
            postImageRepository.save(
                PostImageEntity(
                    id = UUID.randomUUID().toString(),
                    createDate = LocalDateTime.now(),
                    imageUrl = "https://${bucket}.s3.${location}.amazonaws.com/${uniqueFileName}",
                    postUUID = id
                )
            )
        else
            userImageRepository.save(
                UserImageEntity(
                    id = id,
                    imageUrl = "https://${bucket}.s3.${location}.amazonaws.com/${uniqueFileName}"
                )
            )
        return PreSignedUrlResponseDto(
            amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString(),
            generateFileName(fileName)
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

    private fun generateFileName(originalFileName: String): String {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(Date())
        return "$timeStamp.$originalFileName"
    }

    private fun getPreSignedUrlExpiration(): Date {
        val expiration = Date()
        var expTimeMillis: Long = expiration.time
        expTimeMillis += (1000 * 10).toLong()
        expiration.setTime(expTimeMillis)
        return expiration
    }

    private fun getCurrentDateInFormat(): String {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd/")
        return currentDateTime.format(formatter)
    }

}