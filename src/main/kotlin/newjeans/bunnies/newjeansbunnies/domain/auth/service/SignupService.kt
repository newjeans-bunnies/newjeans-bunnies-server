package newjeans.bunnies.newjeansbunnies.domain.auth.service


import newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.request.SignupRequestDto
import newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.response.SignupResponseDto
import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.CountryNotFoundException
import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.ExistIdException
import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.ExistPhoneNumberException
import newjeans.bunnies.newjeansbunnies.domain.auth.type.Authority
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
    @Value("\${support.country}")
    private val countryList: String
) {

    private val countries = countryList.split(",").toSet()

    fun execute(data: SignupRequestDto): SignupResponseDto {

        checkValidUserId(data.userId)

        checkValidPhoneNumber(data.phoneNumber)

        checkValidCountry(data.country)

        val userEntity = UserEntity(
            id = UUID.randomUUID().toString(),
            userId = data.userId,
            password = data.password,
            phoneNumber = data.phoneNumber,
            imageUrl = "https://newjeans-bunnies-image.s3.ap-northeast-2.amazonaws.com/user-image/UserImage.jpg",
            country = data.country,
            language = data.country,
            authority = Authority.USER,
            birth = data.birth
        )

        userEntity.hashPassword(passwordEncoder)

        userRepository.save(userEntity)

        return SignupResponseDto(
            status = 201,
            message = "Created"
        )
    }

    private fun checkValidPhoneNumber(phoneNumber: String) {
        val existingUser = userRepository.findByPhoneNumber(phoneNumber)
        if (existingUser.isPresent) {
            throw ExistPhoneNumberException // 이미 존재하는 전화번호 예외 처리
        }
    }

    private fun checkValidUserId(userId: String) {
        if (userRepository.findByUserId(userId).isPresent && userId == userRepository.findByUserId(userId).get().userId)
            throw ExistIdException // 이미 존재하는 아이디
    }

    private fun checkValidCountry(country: String) {
        if (country !in countries)
            throw CountryNotFoundException // 지원 하지 않거나 존재하지 않는 나라
    }

}