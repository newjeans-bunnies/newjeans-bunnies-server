package newjeans.bunnies.newjeansbunnies.domain.post.service


import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import kotlinx.coroutines.*
import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.NotExistUserIdException
import newjeans.bunnies.newjeansbunnies.domain.post.PostEntity
import newjeans.bunnies.newjeansbunnies.domain.post.PostImageEntity
import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.request.PostRequestDto
import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response.PostResponseDto
import newjeans.bunnies.newjeansbunnies.domain.post.error.exception.OverFlieException
import newjeans.bunnies.newjeansbunnies.domain.post.repository.PostImageRepository
import newjeans.bunnies.newjeansbunnies.domain.post.repository.PostRepository
import newjeans.bunnies.newjeansbunnies.domain.user.repository.UserRepository
import newjeans.bunnies.newjeansbunnies.global.config.AwsS3Config
import newjeans.bunnies.newjeansbunnies.global.error.exception.BlankFileNameException
import newjeans.bunnies.newjeansbunnies.global.utils.CheckFileExtension
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


@Configuration
@Service
class CreatePostService(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
    private val postImageRepository: PostImageRepository,
    private val checkFileExtension: CheckFileExtension,
    private val awsS3Config: AwsS3Config,
    @Value("\${cloud.aws.s3.bucket}")
    private val bucket: String,
    @Value("\${cloud.aws.region.static}")
    private val location: String,
    @Value("\${support.fileFormat}")
    private val fileFormat: String
) {

    private val fileFormats = fileFormat.split(",").toSet()

    suspend fun execute(postData: PostRequestDto, multipartFile: List<MultipartFile>): PostResponseDto {

        checkValidUserId(postData.userId)

        if (multipartFile.size > 10)
            throw OverFlieException

        val postUUID = UUID.randomUUID().toString()

        val postEntity = PostEntity(
            uuid = postUUID,
            userId = postData.userId,
            body = postData.body,
            createDate = LocalDateTime.now().toString(),
            good = 0
        )

        multipartFile.map {
            val extension: String = StringUtils.getFilenameExtension(it.originalFilename) ?: throw BlankFileNameException
            checkFileExtension.execute(extension)
        }

        CoroutineScope(Dispatchers.IO).launch {
            uploadMultipleFiles(multipartFile, postEntity.uuid)
        }

        postRepository.save(postEntity)

        return PostResponseDto(
            postId = postEntity.uuid,
            createDate = postEntity.createDate
        )
    }
    private suspend fun getCurrentDateInFormat(): String {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        return currentDateTime.format(formatter)
    }


    private suspend fun uploadMultipleFiles(multipartFile: List<MultipartFile>, postId: String) = withContext(Dispatchers.IO) {
        val uploadJobs = multipartFile.map {
            val objectMetadata = ObjectMetadata().apply {
                this.contentType = it.contentType
                this.contentLength = it.size
            }
            async {
                val putObjectRequest = PutObjectRequest(
                    bucket,
                    "post-image/"+getCurrentDateInFormat()+"/"+"$postId/"+UUID.randomUUID().toString(),
                    it.inputStream,
                    objectMetadata,
                )
                awsS3Config.amazonS3Client().putObject(putObjectRequest)
                postImageRepository.save(PostImageEntity(
                    imageUrl = "https://${bucket}.s3.${location}.amazonaws.com/"+putObjectRequest.key,
                    createDate = LocalDateTime.now().toString(),
                    postId = postId
                ))
            }
        }
        uploadJobs.awaitAll()
    }

    private suspend fun checkValidUserId(userId: String){
        userRepository.findByUserId(userId).orElseThrow {
            throw NotExistUserIdException
        }
    }
}