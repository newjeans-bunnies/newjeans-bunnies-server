package newjeans.bunnies.newjeansbunnies.domain.user.service


import jakarta.transaction.Transactional

import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.*
import newjeans.bunnies.newjeansbunnies.domain.image.service.UserImageService
import newjeans.bunnies.newjeansbunnies.domain.user.UserEntity
import newjeans.bunnies.newjeansbunnies.domain.user.controller.dto.request.UserUpdateRequestDto
import newjeans.bunnies.newjeansbunnies.domain.user.controller.dto.response.UserUpdateResponseDto
import newjeans.bunnies.newjeansbunnies.domain.user.repository.UserRepository
import newjeans.bunnies.newjeansbunnies.global.config.AwsS3Config

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
    private val awsS3Config: AwsS3Config,
    private val userImageService: UserImageService,
) {
    private val countries = countryList.split(",").toSet()
    private val fileFormats = fileFormat.split(",").toSet()

    private var imageUrl: String? = null
    private var preSignedURL: String? = null

    @Transactional
    fun execute(id: String, userUpdateRequestDto: UserUpdateRequestDto): UserUpdateResponseDto {

        val userData = userRepository.findByUserId(id).orElseThrow {
            throw NotExistIdException
        }

        if (id != userUpdateRequestDto.userId && userRepository.findByUserId(userUpdateRequestDto.userId).isPresent)
            throw ExistIdException //이미 존재하는 아이디


        if (userRepository.findByPhoneNumber(userUpdateRequestDto.phoneNumber).isPresent && userUpdateRequestDto.phoneNumber != userData.phoneNumber)
            throw ExistPhoneNumberException //이미 존재하는 전화번호

        if (userUpdateRequestDto.country !in countries)
            throw CountryNotFoundException //지원 하지 않거나 존재하지 않는 나라

        //기존의 이미지가 널이 아닐경우
        if (userData.imageUrl != null) {
            try {
                awsS3Config.amazonS3Client().deleteObject(bucket, "user-image/${userUpdateRequestDto.userId}.webp")
            } catch (e: Exception) {
                println(e.message)
            }
        }





        if (userUpdateRequestDto.imageName != null) {

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
                country = userUpdateRequestDto.country
            )
        )
        return UserUpdateResponseDto(
            id = userUpdateRequestDto.userId,
            phoneNumber = userUpdateRequestDto.phoneNumber,
            imageUrl = imageUrl,
            country = userUpdateRequestDto.country,
            preSignedURL = preSignedURL
        )
    }
}