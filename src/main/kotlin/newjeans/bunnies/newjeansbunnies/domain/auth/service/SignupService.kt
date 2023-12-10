package newjeans.bunnies.newjeansbunnies.domain.auth.service


import newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.request.SignupRequestDto
import newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.response.SignupResponseDto
import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.*
import newjeans.bunnies.newjeansbunnies.domain.image.service.UserImageService
import newjeans.bunnies.newjeansbunnies.domain.user.UserEntity
import newjeans.bunnies.newjeansbunnies.domain.user.repository.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*


@Service
@Configuration
class SignupService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val userImageService: UserImageService,
    @Value("\${cloud.aws.s3.bucket}")
    private val bucket: String,
    @Value("\${cloud.aws.region.static}")
    private val location: String,
    @Value("\${support.country}")
    private val countryList: String,
    @Value("\${support.fileFormat}")
    private val fileFormat: String
) {

    private val countries = countryList.split(",").toSet()
    private val fileFormats = fileFormat.split(",").toSet()

    fun execute(data: SignupRequestDto): SignupResponseDto {

        if (userRepository.findByUserId(data.userId).isPresent)
            throw ExistIdException //이미 존재하는 아이디

        if (userRepository.findByPhoneNumber(data.phoneNumber).isPresent)
            throw ExistPhoneNumberException //이미 존재하는 전화번호

        if (data.country !in countries)
            throw CountryNotFoundException //지원 하지 않거나 존재하지 않는 나라

        if (data.language !in countries)
            throw LanguageNotFoundException //지원 하지 않거나 존재하지 않는 나라

        if (!data.imageName.isNullOrBlank()) { //파일이름이 null이 아니라면

            val fileExtension = data.imageName.substringAfterLast('.', "")
            if (fileExtension !in fileFormats)
                throw FormatNotSupportedException //지원하지 않거나 존재하지 않는 확장자

            val preSignedURL = userImageService.getPreSignedUrl(data.imageName, data.userId).preSignedURL

            val imageUrl = "https://${bucket}.s3.${location}.amazonaws.com/user-image/${data.userId}.$fileExtension"

            val userEntity = UserEntity(
                uuid = UUID.randomUUID().toString(),
                userId = data.userId,
                password = data.password,
                phoneNumber = data.phoneNumber,
                imageUrl = imageUrl,
                country = data.country,
                language = data.language,
                birth = data.birth
            )
            userEntity.hashPassword(passwordEncoder)
            userRepository.save(userEntity)

            //회원가입 할때 사진도 같이 할거면 사진을 넣을 URL을 보내줌
            return SignupResponseDto(
                preSignedURL = preSignedURL,
                status = 201,
                message = "Created"
            )
        }

        val userEntity = UserEntity(
            uuid = UUID.randomUUID().toString(),
            userId = data.userId,
            password = data.password,
            phoneNumber = data.phoneNumber,
            imageUrl = null,
            country = data.country,
            language = data.language,
            birth = data.birth
        )
        userEntity.hashPassword(passwordEncoder)
        userRepository.save(userEntity)

        return SignupResponseDto(
            preSignedURL = null,
            status = 201,
            message = "Created"
        )
    }


}