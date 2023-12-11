package newjeans.bunnies.newjeansbunnies.domain.image.service

import newjeans.bunnies.newjeansbunnies.global.config.AwsS3Config
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import java.net.URL

@Service
@Configuration
class DeletePostImageService(
    @Value("\${cloud.aws.s3.bucket}")
    private val bucket: String,
    private val awsS3Config: AwsS3Config,
) {

    fun deletePostImage(imageUrl: String) {
        awsS3Config.amazonS3Client().deleteObject(bucket, extractPathAndFileName(imageUrl))
    }

    private fun extractPathAndFileName(url: String): String {
        val urlObject = URL(url)
        val path = urlObject.path
        return path.trim('/')
    }

}