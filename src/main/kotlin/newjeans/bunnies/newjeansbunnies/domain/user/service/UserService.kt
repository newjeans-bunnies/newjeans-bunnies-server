package newjeans.bunnies.newjeansbunnies.domain.user.service

import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.*
import newjeans.bunnies.newjeansbunnies.domain.user.UserEntity
import newjeans.bunnies.newjeansbunnies.domain.user.controller.dto.request.UserUpdateRequestDto
import newjeans.bunnies.newjeansbunnies.domain.user.controller.dto.response.UserDataBasicInfoResponseDto
import newjeans.bunnies.newjeansbunnies.domain.user.controller.dto.response.UserDataDetailsResponseDto
import newjeans.bunnies.newjeansbunnies.domain.user.controller.dto.response.UserImageResponseDto
import newjeans.bunnies.newjeansbunnies.domain.user.controller.dto.response.UserSupportResponseDto
import newjeans.bunnies.newjeansbunnies.domain.user.error.exception.InactiveUserException
import newjeans.bunnies.newjeansbunnies.domain.user.error.exception.RuleViolationNicknameException
import newjeans.bunnies.newjeansbunnies.domain.user.repository.UserRepository
import newjeans.bunnies.newjeansbunnies.global.response.StatusResponseDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
@Configuration
class UserService(
    private val userRepository: UserRepository,
    @Value("\${support.country}") private val country: String,
    @Value("\${support.fileFormat}") private val fileFormat: String
) {

    // 도움 정보
    private val countries = country.split(",").toList()
    private val fileFormats = fileFormat.split(",").toList()

    // 아이디 정규식
    private val idRegex = Regex("^([a-zA-Z0-9]){1,12}\$")

    // 유효한 유저아이디 확인
    fun checkExistNickname(nickname: String): UserEntity {
        val user = userRepository.findByNickname(nickname).orElseThrow {
            throw NotExistNicknameException
        }

        // DB에서 대소문자 구분을 못해서 이쪽에서 구분
        if (nickname != user.nickname) throw NotExistNicknameException

        return user
    }

    fun checkActivationUser(nickname: String): UserEntity {
        val user = userRepository.findByNickname(nickname).orElseThrow {
            throw NotExistNicknameException
        }

        if (!user.state) throw InactiveUserException

        return user
    }

    // 사용자 사진 가져오기
    fun getUserImage(userId: String): UserImageResponseDto {
        val imageURL = (userRepository.findByIdOrNull(userId) ?: throw NotExistNicknameException).imageUrl
        return UserImageResponseDto(imageURL)
    }

    // 사용중인 유저 아이디인지 확인
    fun nickname(nickname: String): StatusResponseDto {
        if (userRepository.findByNickname(nickname).isPresent) throw ExistIdException

        if (!nicknamePattern(nickname)) throw RuleViolationNicknameException

        return StatusResponseDto(
            status = 200, message = "Ok"
        )
    }

    // 아이디 패턴 확인
    fun nicknamePattern(input: String): Boolean {
        return idRegex.matches(input)
    }

    // 유저 업데이트
    fun updateUserImage(
        authorizedUser: String?,
        userUpdateRequestDto: UserUpdateRequestDto,
    ): StatusResponseDto {

        val userData = userRepository.findByNickname(authorizedUser).orElseThrow {
            throw NotExistNicknameException
        }

        if (nicknamePattern(userUpdateRequestDto.nickname)) throw RuleViolationNicknameException

        if (authorizedUser != userUpdateRequestDto.nickname && userRepository.findByNickname(userUpdateRequestDto.nickname).isPresent) throw ExistIdException //이미 존재하는 아이디

        if (userUpdateRequestDto.country !in countries) throw CountryNotFoundException //지원 하지 않거나 존재하지 않는 나라

        if (userUpdateRequestDto.language !in countries) throw LanguageNotFoundException //지원 하지 않거나 존재하지 않는 언어

        with(userData) {
            nickname = userUpdateRequestDto.nickname
            country = userUpdateRequestDto.country
            birth = userUpdateRequestDto.birth
            language = userUpdateRequestDto.language
        }

        userRepository.save(userData)

        return StatusResponseDto(200, "Ok")
    }

    // 사용중인 전화번호인지 확인
    fun phoneNumber(phoneNumber: String): StatusResponseDto {
        if (userRepository.findByPhoneNumber(phoneNumber).isPresent) throw ExistPhoneNumberException

        return StatusResponseDto(
            status = 200, message = "Ok"
        )
    }

    // 유저 정보 가져오기
    fun getUserData(userId: String): UserDataBasicInfoResponseDto {
        val userData = userRepository.findByIdOrNull(userId) ?: throw NotExistNicknameException

        return UserDataBasicInfoResponseDto(
            nickname = userData.nickname, country = userData.country, imageUrl = userData.imageUrl
        )
    }

    // 내 정보 가져오기
    fun getMyData(userId: String): UserDataDetailsResponseDto {

        val userData = userRepository.findByIdOrNull(userId) ?: throw NotExistNicknameException

        return UserDataDetailsResponseDto(
            nickname = userData.nickname,
            country = userData.country,
            imageUrl = userData.imageUrl,
            birth = userData.birth,
            language = userData.language,
            phoneNumber = userData.phoneNumber
        )
    }

    // 도움 정보 가져오기
    fun getSupport(): UserSupportResponseDto {
        return UserSupportResponseDto(
            countries, fileFormats
        )
    }

}