package newjeans.bunnies.newjeansbunnies.domain.user.service


import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import jakarta.transaction.Transactional
import kotlinx.coroutines.*
import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.CountryNotFoundException
import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.ExistIdException
import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.LanguageNotFoundException
import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.NotExistUserIdException
import newjeans.bunnies.newjeansbunnies.domain.user.UserEntity
import newjeans.bunnies.newjeansbunnies.domain.user.controller.dto.request.UserUpdateRequestDto
import newjeans.bunnies.newjeansbunnies.domain.user.controller.dto.response.UserUpdateResponseDto
import newjeans.bunnies.newjeansbunnies.domain.user.repository.UserRepository
import newjeans.bunnies.newjeansbunnies.global.config.AwsS3Config
import newjeans.bunnies.newjeansbunnies.global.error.exception.BlankFileNameException
import newjeans.bunnies.newjeansbunnies.global.utils.CheckFileExtension
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile


@Service
@Configuration
class UserUpdateService(
    @Value("\${cloud.aws.s3.bucket}")
    private val bucket: String,
    @Value("\${cloud.aws.region.static}")
    private val location: String,
    private val userRepository: UserRepository,
    @Value("\${support.country}")
    private val countryList: String,
    private val awsS3Config: AwsS3Config,
    private val checkFileExtension: CheckFileExtension
) {
    private val countries = countryList.split(",").toSet()

    var userImageURL: String? = null

    @Transactional
    suspend fun execute(
        userId: String,
        userUpdateRequestDto: UserUpdateRequestDto,
        multipartFiles: MultipartFile?
    ): UserUpdateResponseDto {

        val userData = userRepository.findByUserId(userId).orElseThrow {
            throw NotExistUserIdException
        }

        if (userId != userUpdateRequestDto.userId && userRepository.findByUserId(userUpdateRequestDto.userId).isPresent)
            throw ExistIdException //이미 존재하는 아이디

        if (userUpdateRequestDto.country !in countries)
            throw CountryNotFoundException //지원 하지 않거나 존재하지 않는 나라

        if (userUpdateRequestDto.language !in countries)
            throw LanguageNotFoundException //지원 하지 않거나 존재하지 않는 나라

        if (multipartFiles != null) {
            CoroutineScope(Dispatchers.IO).launch {
                val extension: String = StringUtils.getFilenameExtension(multipartFiles.originalFilename) ?: throw BlankFileNameException
                checkFileExtension.execute(extension)
                async {
                    userImageURL = uploadMultipleFiles(multipartFiles, userData.uuid).await()
                    userRepository.save(
                        UserEntity(
                            uuid = userData.uuid,
                            userId = userUpdateRequestDto.userId,
                            password = userData.password,
                            phoneNumber = userData.phoneNumber,
                            imageUrl = userImageURL,
                            country = userUpdateRequestDto.country,
                            language = userUpdateRequestDto.language,
                            authority = userData.authority,
                            birth = userUpdateRequestDto.birth
                        )
                    )
                }.await()
            }
        } else {
            userRepository.save(
                UserEntity(
                    uuid = userData.uuid,
                    userId = userUpdateRequestDto.userId,
                    password = userData.password,
                    phoneNumber = userData.phoneNumber,
                    imageUrl = null,
                    country = userUpdateRequestDto.country,
                    language = userUpdateRequestDto.language,
                    authority = userData.authority,
                    birth = userUpdateRequestDto.birth
                )
            )

            return UserUpdateResponseDto(
                id = userUpdateRequestDto.userId,
                imageUrl = null,
                country = userUpdateRequestDto.country,
                language = userUpdateRequestDto.language
            )
        }

        return UserUpdateResponseDto(
            id = userUpdateRequestDto.userId,
            imageUrl = userImageURL,
            country = userUpdateRequestDto.country,
            language = userUpdateRequestDto.language
        )
    }

    private suspend fun uploadMultipleFiles(multipartFile: MultipartFile, id: String) = withContext(
        Dispatchers.IO
    ) {
        val objectMetadata = ObjectMetadata().apply {
            this.contentType = multipartFile.contentType
            this.contentLength = multipartFile.size
        }
        async {
            val putObjectRequest = PutObjectRequest(
                bucket,
                "user-image/$id",
                multipartFile.inputStream,
                objectMetadata,
            )
            awsS3Config.amazonS3Client().putObject(putObjectRequest)
            "https://${bucket}.s3.${location}.amazonaws.com/user-image/$id"
        }
    }
}