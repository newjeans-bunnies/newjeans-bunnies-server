package newjeans.bunnies.newjeansbunnies.domain.auth.service

import newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.TokenDto
import newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.request.LoginRequestDto
import newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.request.SignupRequestDto
import newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.response.SignupResponseDto
import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.*
import newjeans.bunnies.newjeansbunnies.domain.auth.type.Authority
import newjeans.bunnies.newjeansbunnies.domain.post.service.PostService
import newjeans.bunnies.newjeansbunnies.domain.user.UserEntity
import newjeans.bunnies.newjeansbunnies.domain.user.error.exception.BlankUserIdException
import newjeans.bunnies.newjeansbunnies.domain.user.repository.UserRepository
import newjeans.bunnies.newjeansbunnies.domain.user.service.UserService
import newjeans.bunnies.newjeansbunnies.global.error.exception.InvalidRoleException
import newjeans.bunnies.newjeansbunnies.global.response.StatusResponseDto
import newjeans.bunnies.newjeansbunnies.global.security.jwt.JwtProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Configuration
@Service
class AuthService(
    private val userRepository: UserRepository,
    private val userService: UserService,
    private val postService: PostService,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val jwtProvider: JwtProvider,
    @Value("\${support.country}")
    private val countryList: String
) {

    private val countries = countryList.split(",").toSet()

    // 계정 비활성화
    fun userDelete(userId: String, authorizedUser: String?): StatusResponseDto {
        if (userId.isBlank()) throw BlankUserIdException

        if (userId != authorizedUser) throw InvalidRoleException

        val user = userService.checkExistUserId(userId)

        val posts = postService.getPostByUserId(userId)

        posts.map { postData ->
            postService.disabledPost(postData.id, authorizedUser)
        }

        user.state = false

        userRepository.save(user)

        return StatusResponseDto(204, "계정이 삭제 되었습니다.")
    }

    // 로그인
    fun login(data: LoginRequestDto): TokenDto {

        val userData = getExistUserId(data.userId)
        checkExistUserId(userData.userId, data.userId)

        matchesPassword(data.password, userData.password)

        return jwtProvider.receiveToken(userData.id, userData.authority)
    }

    // 회원가입
    fun signup(data: SignupRequestDto): SignupResponseDto {

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

    private fun getExistUserId(userId: String) = userRepository.findByUserId(userId).orElseThrow {
        throw NotExistUserIdException
    }

    private fun matchesPassword(password: String, sparePassword: String) {
        if (!passwordEncoder.matches(password, sparePassword)) throw InvalidPasswordException
    }

    private fun checkExistUserId(userId: String, spareUserId: String) {
        if (userId != spareUserId) throw NotExistUserIdException
    }
}