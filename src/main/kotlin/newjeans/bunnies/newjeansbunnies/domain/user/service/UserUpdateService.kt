package newjeans.bunnies.newjeansbunnies.domain.user.service


import jakarta.transaction.Transactional

import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.*
import newjeans.bunnies.newjeansbunnies.domain.image.service.UserImageService
import newjeans.bunnies.newjeansbunnies.domain.user.UserEntity
import newjeans.bunnies.newjeansbunnies.domain.user.controller.dto.request.UserUpdateRequestDto
import newjeans.bunnies.newjeansbunnies.domain.user.controller.dto.response.UserUpdateResponseDto
import newjeans.bunnies.newjeansbunnies.domain.user.repository.UserRepository

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service


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
    @Value("\${support.fileFormat}")
    private val fileFormat: String,
    private val userImageService: UserImageService,
) {
    private val countries = countryList.split(",").toSet()
    private val fileFormats = fileFormat.split(",").toSet()

    private var imageUrl: String? = null
    private var preSignedURL: String? = null

    @Transactional
    fun execute(userId: String, userUpdateRequestDto: UserUpdateRequestDto): UserUpdateResponseDto {

        val userData = userRepository.findByUserId(userId).orElseThrow {
            throw NotExistUserIdException
        }

        if (userId != userUpdateRequestDto.userId && userRepository.findByUserId(userUpdateRequestDto.userId).isPresent)
            throw ExistIdException //이미 존재하는 아이디


        if (userRepository.findByPhoneNumber(userUpdateRequestDto.phoneNumber).isPresent && userUpdateRequestDto.phoneNumber != userData.phoneNumber)
            throw ExistPhoneNumberException //이미 존재하는 전화번호

        if (userUpdateRequestDto.country !in countries)
            throw CountryNotFoundException //지원 하지 않거나 존재하지 않는 나라

        if (userUpdateRequestDto.language !in countries)
            throw LanguageNotFoundException //지원 하지 않거나 존재하지 않는 나라

        //기존의 이미지가 널이 아닐경우
        if (userData.imageUrl != null) {
            userImageService.deleteUserImage(userData.imageUrl, userId)
        }


        if (!userUpdateRequestDto.imageName.isNullOrBlank()) {

            val fileExtension = userUpdateRequestDto.imageName.substringAfterLast('.', "")

            if (fileExtension !in fileFormats)
                throw FormatNotSupportedException //지원하지 않거나 존재하지 않는 확장자

            imageUrl = "https://${bucket}.s3.${location}.amazonaws.com/user-image/${userUpdateRequestDto.userId}.webp"
            preSignedURL = userImageService.getPreSignedUrl(
                userUpdateRequestDto.userId + ".webp",
                userUpdateRequestDto.userId
            ).preSignedURL
        }

        userRepository.save(
            UserEntity(
                uuid = userData.uuid,
                userId = userUpdateRequestDto.userId,
                password = userData.password,
                phoneNumber = userUpdateRequestDto.phoneNumber,
                imageUrl = imageUrl,
                country = userUpdateRequestDto.country,
                language = userUpdateRequestDto.language,
            )
        )
        return UserUpdateResponseDto(
            id = userUpdateRequestDto.userId,
            phoneNumber = userUpdateRequestDto.phoneNumber,
            imageUrl = imageUrl,
            country = userUpdateRequestDto.country,
            preSignedURL = preSignedURL,
            language = userUpdateRequestDto.language
        )
    }
}