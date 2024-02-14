package newjeans.bunnies.newjeansbunnies.domain.auth.service


import newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.request.SignupRequestDto
import newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.response.SignupResponseDto
import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.CountryNotFoundException
import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.ExistIdException
import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.ExistPhoneNumberException
import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.LanguageNotFoundException
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

        if (userRepository.findByUserId(data.userId).isPresent && data.userId == userRepository.findByUserId(data.userId).get().userId)
            throw ExistIdException //이미 존재하는 아이디

        if (userRepository.findByPhoneNumber(data.phoneNumber).isPresent)
            throw ExistPhoneNumberException //이미 존재하는 전화번호

        if (data.country !in countries)
            throw CountryNotFoundException //지원 하지 않거나 존재하지 않는 나라

        if (data.language !in countries)
            throw LanguageNotFoundException //지원 하지 않거나 존재하지 않는 언어

        val userEntity = UserEntity(
            uuid = UUID.randomUUID().toString(),
            userId = data.userId,
            password = data.password,
            phoneNumber = data.phoneNumber,
            imageUrl = null,
            country = data.country,
            language = data.language,
        )
        userEntity.hashPassword(passwordEncoder)
        userRepository.save(userEntity)

        return SignupResponseDto(
            status = 201,
            message = "Created"
        )
    }
}