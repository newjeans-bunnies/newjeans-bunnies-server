package newjeans.bunnies.newjeansbunnies.domain.user.service


import newjeans.bunnies.newjeansbunnies.global.config.AwsS3Config
import org.hibernate.query.sqm.tree.SqmNode.log
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service

@Service
@Configuration
class DeleteUserImageService(
    @Value("\${cloud.aws.s3.bucket}")
    private val bucket: String,
    private val awsS3Config: AwsS3Config,
) {
    fun execute(id: String) {
        try {
            awsS3Config.amazonS3Client().deleteObject(bucket, "user-image/$id")
        } catch (e: Exception) {
            log.info(e.message)
        }
    }
}