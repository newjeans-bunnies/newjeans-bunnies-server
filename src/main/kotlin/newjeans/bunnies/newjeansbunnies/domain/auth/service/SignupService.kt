package newjeans.bunnies.newjeansbunnies.domain.auth.service

import newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.request.SignupRequestDto
import newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.response.StatusResponseDto
import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.ExistIdException
import newjeans.bunnies.newjeansbunnies.domain.auth.type.Authority
import newjeans.bunnies.newjeansbunnies.domain.user.UserEntity
import newjeans.bunnies.newjeansbunnies.domain.user.repository.UserRepository
import newjeans.bunnies.newjeansbunnies.global.config.AwsS3Config
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
@Configuration
class SignupService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    @Value("\${cloud.aws.s3.bucket}")
    private val bucket: String,
    @Value("\${cloud.aws.region.static}")
    private val location: String,
) {
    fun execute(data: SignupRequestDto): StatusResponseDto {
        if(userRepository.findById(data.id).isPresent)
            throw ExistIdException
        var imageUrl: String? = null
        if(!data.imageName.isNullOrBlank()){ //파일이름이 null이 아니라면
            val fileExtension = data.imageName.substringAfterLast('.', "")
            imageUrl = "https://${bucket}.s3.${location}.amazonaws.com/user-image/${data.id}.$fileExtension"
        }

        val userEntity = UserEntity(
            id = data.id,
            password = data.password,
            phoneNumber = data.phoneNumber,
            imageUrl = imageUrl,
            authority = Authority.USER.name
        )
        userEntity.hashPassword(passwordEncoder)
        userRepository.save(userEntity)

        return StatusResponseDto(
            201,
            "Created"
        )
    }


}