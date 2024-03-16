package newjeans.bunnies.newjeansbunnies.domain.post.service

import jakarta.transaction.Transactional
import newjeans.bunnies.newjeansbunnies.global.config.AwsS3Config
import newjeans.bunnies.newjeansbunnies.global.error.exception.InternalServerErrorException
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service

@Service
@Configuration
class DeletePostImageService(
    @Value("\${cloud.aws.s3.bucket}")
    private val bucket: String,
    @Value("\${cloud.aws.region.static}")
    private val location: String,
    private val awsS3Config: AwsS3Config,
) {

    @Transactional
    suspend fun deletePostImage(imageUrl: String) {
        val key = removeUrlPrefix(imageUrl)
        awsS3Config.amazonS3Client().deleteObject(bucket, key)
    }

    fun removeUrlPrefix(fullUrl: String): String {
        val prefix = "https://${bucket}.s3.${location}.amazonaws.com/"

        return if (fullUrl.startsWith(prefix)) {
            fullUrl.substring(prefix.length)
        } else {
            throw InternalServerErrorException
        }
    }

}