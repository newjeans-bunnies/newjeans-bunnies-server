package newjeans.bunnies.newjeansbunnies.domain.image.service

import com.amazonaws.services.s3.model.DeleteObjectRequest
import jakarta.transaction.Transactional
import newjeans.bunnies.newjeansbunnies.global.config.AwsS3Config
import newjeans.bunnies.newjeansbunnies.global.response.StatusResponseDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service

@Service
@Configuration
class AwsUploadService(
    private val awsS3Config: AwsS3Config,
    @Value("\${cloud.aws.s3.bucket}") private val bucket: String,
    private val imageService: ImageService
) {


    // 업로드 한 파일 확정
    fun completeUpload(postId: String): StatusResponseDto {
        val images = imageService.getImageByPostId(postId)
        images.map {
            imageService.activationImage(it.imageId)
        }

        return StatusResponseDto(200, "사진이 활성됨")
    }


    @Transactional
    // 비활성화되어 있는 사진 삭제 하기
    fun deleteMultipartUpload(imageId: String): StatusResponseDto {

        val image = imageService.getImage(imageId)

        // 사진 아이디를 사용해 사진 삭제
        imageService.deleteImage(imageId)

        // aws s3 객체 삭제
        awsS3Config.amazonS3Client().deleteObject(DeleteObjectRequest(bucket, image.imageKey))

        return StatusResponseDto(204, "사진이 삭제됨")
    }


}
