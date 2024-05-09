package newjeans.bunnies.newjeansbunnies.domain.user.service

import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.ExistIdException
import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.ExistPhoneNumberException
import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.NotExistUserIdException
import newjeans.bunnies.newjeansbunnies.domain.user.UserEntity
import newjeans.bunnies.newjeansbunnies.domain.user.controller.dto.response.UserDataBasicInfoResponseDto
import newjeans.bunnies.newjeansbunnies.domain.user.controller.dto.response.UserDataDetailsResponseDto
import newjeans.bunnies.newjeansbunnies.domain.user.controller.dto.response.UserImageResponseDto
import newjeans.bunnies.newjeansbunnies.domain.user.controller.dto.response.UserSupportResponseDto
import newjeans.bunnies.newjeansbunnies.domain.user.error.exception.InactiveUserException
import newjeans.bunnies.newjeansbunnies.domain.user.error.exception.RuleViolationUserIdException
import newjeans.bunnies.newjeansbunnies.domain.user.repository.UserRepository
import newjeans.bunnies.newjeansbunnies.global.error.exception.InvalidTokenException
import newjeans.bunnies.newjeansbunnies.global.response.StatusResponseDto
import newjeans.bunnies.newjeansbunnies.global.security.jwt.JwtParser
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service

@Service
@Configuration
class UserService(
    private val userRepository: UserRepository,
    @Value("\${support.country}") private val country: String,
    @Value("\${support.fileFormat}") private val fileFormat: String,
    private val jwtParser: JwtParser
) {

    companion object {
        private const val PREFIX = "Bearer "
    }

    // 도움 정보
    private val countries = country.split(",").toList()
    private val fileFormats = fileFormat.split(",").toList()

    // 아이디 정규식
    private val idRegex = Regex("^([a-zA-Z0-9]){1,12}\$")


    // 유효한 유저아이디 확인
    fun checkExistUserId(userId: String): UserEntity {
        val user = userRepository.findByUserId(userId)
            .orElseThrow {
                throw NotExistUserIdException
            }

        // DB에서 대소문자 구분을 못해서 이쪽에서 구분
        if (userId != user.userId)
            throw NotExistUserIdException

        return user
    }

    fun checkActivationUser(userId: String): UserEntity {
        val user = userRepository.findByUserId(userId).orElseThrow {
            throw NotExistUserIdException
        }

        if(!user.state)
            throw InactiveUserException

        return user
    }

    // 사용자 사진 가져오기
    fun getUserImage(userId: String): UserImageResponseDto {
        val imageURL = userRepository.findByUserId(userId).orElseThrow {
            throw NotExistUserIdException
        }.imageUrl

        return UserImageResponseDto(imageURL)
    }


    // 사용중인 유저 아이디인지 확인
    fun userId(userId: String): StatusResponseDto {
        if (userRepository.findByUserId(userId).isPresent && userId == userRepository.findByUserId(userId).get().userId)
            throw ExistIdException

        if (!idPattern(userId))
            throw RuleViolationUserIdException

        return StatusResponseDto(
            status = 200,
            message = "Ok"
        )
    }

    // 아이디 패턴 확인
    fun idPattern(input: String): Boolean {
        return idRegex.matches(input)
    }

    // 사용중인 전화번호인지 확인
    fun phoneNumber(phoneNumber: String): StatusResponseDto {
        if (userRepository.findByPhoneNumber(phoneNumber).isPresent)
            throw ExistPhoneNumberException

        return StatusResponseDto(
            status = 200,
            message = "Ok"
        )
    }

    // 유저 정보 가져오기
    fun getUserData(id: String): UserDataBasicInfoResponseDto {
        val userData = userRepository.findByUserId(id).orElseThrow {
            throw NotExistUserIdException
        }

        return UserDataBasicInfoResponseDto(
            userId = userData.userId,
            country = userData.country,
            imageUrl = userData.imageUrl
        )
    }

    fun getMyData(token: String): UserDataDetailsResponseDto {
        if (!token.startsWith(PREFIX)) {
            throw InvalidTokenException
        }

        val id = jwtParser.getClaims(token.removePrefix(PREFIX)).body.id

        val userData = userRepository.findById(id).orElseThrow {
            throw NotExistUserIdException
        }

        return UserDataDetailsResponseDto(
            userId = userData.userId,
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