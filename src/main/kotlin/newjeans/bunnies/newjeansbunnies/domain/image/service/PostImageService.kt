package newjeans.bunnies.newjeansbunnies.domain.image.service


import com.amazonaws.HttpMethod
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.Headers
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.FormatNotSupportedException
import newjeans.bunnies.newjeansbunnies.domain.image.PostImageEntity
import newjeans.bunnies.newjeansbunnies.domain.image.controller.dto.response.PostImagePreSignedUrlResponseDto
import newjeans.bunnies.newjeansbunnies.domain.image.error.exception.ImageExtensionNotForundException
import newjeans.bunnies.newjeansbunnies.domain.image.repository.PostImageRepository
import newjeans.bunnies.newjeansbunnies.domain.post.error.exception.NotExistPostIdException
import newjeans.bunnies.newjeansbunnies.domain.post.repository.PostRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


@Service
@Configuration
class PostImageService(
    @Value("\${cloud.aws.s3.bucket}")
    private val bucket: String,
    @Value("\${cloud.aws.region.static}")
    private val location: String,
    private val amazonS3: AmazonS3,
    private val postImageRepository: PostImageRepository,
    private val postRepository: PostRepository,
    @Value("\${support.fileFormat}")
    private val fileFormat: String
) {

    private val fileFormats = fileFormat.split(",").toSet()

    fun getPreSignedUrl(imageName: String, postId: String): PostImagePreSignedUrlResponseDto {

        postRepository.findById(postId).orElseThrow {
            throw NotExistPostIdException
        }

        val fileExtension = imageName.substringAfterLast('.', "")

        if(fileExtension.isBlank())
            throw ImageExtensionNotForundException

        if(fileExtension !in fileFormats)
            throw FormatNotSupportedException //지원하지 않거나 존재하지 않는 확장자

        val uniqueFileName = generateFileName(imageName)

        val generatePresignedUrlRequest: GeneratePresignedUrlRequest =
            getGeneratePreSignedUrlRequest(bucket, "post-image/" + getCurrentDateInFormat() + "/" + uniqueFileName)

        postImageRepository.save(
            PostImageEntity(
                imageUrl = "https://${bucket}.s3.${location}.amazonaws.com/post-image/${getCurrentDateInFormat()}/${uniqueFileName}",
                createDate = LocalDateTime.now().toString(),
                postId = postId
            )
        )

        return PostImagePreSignedUrlResponseDto(
            preSignedURL = amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString(),
            imageName = uniqueFileName
        )
    }

    private fun generateFileName(originalFileName: String): String {
        val timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmmssSSS"))
        return "$timeStamp.$originalFileName"
    }

    private fun getCurrentDateInFormat(): String {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        return currentDateTime.format(formatter)
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