package newjeans.bunnies.newjeansbunnies.domain.auth.service

import newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.request.CertificationVerifyRequestDto
import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.ExistPhoneNumberException
import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.FailedAuthenticationException
import newjeans.bunnies.newjeansbunnies.domain.user.repository.UserRepository
import newjeans.bunnies.newjeansbunnies.global.config.RedisConfig
import newjeans.bunnies.newjeansbunnies.global.response.StatusResponseDto
import newjeans.bunnies.newjeansbunnies.global.utils.SmsUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import java.time.Duration
import kotlin.random.Random
import kotlin.random.nextInt

@Service
@Configuration
class PhoneNumberCertificationService(
    private val smsUtil: SmsUtil,
    private val redisConfig: RedisConfig,
    private val userRepository: UserRepository
) {
    private val log: Logger = LoggerFactory.getLogger(PhoneNumberCertificationService::class.java)

    suspend fun verify(certificationVerifyRequestDto: CertificationVerifyRequestDto): StatusResponseDto {
        checkValidCertificationNumber(certificationVerifyRequestDto)

        deleteValues(certificationVerifyRequestDto.phoneNumber)

        return StatusResponseDto(200, "success")
    }

    suspend fun certification(phoneNumber: String): StatusResponseDto {

        checkValidPhoneNumber(phoneNumber)

        val randomNumber = createRandomNumber()

        setValues(phoneNumber, randomNumber)

        log.info("[인증번호: $randomNumber] NewJeans-Bunnies 인증번호입니다.")
//        smsUtil.sendOne(phoneNumber, "[인증번호: $randomNumber] NewJeans-Bunnies 인증번호입니다.")

        return StatusResponseDto(200, "success")
    }

    private suspend fun checkValidPhoneNumber(phoneNumber: String){
        if (userRepository.findByPhoneNumber(phoneNumber).isPresent)
            throw ExistPhoneNumberException
    }

    private fun createRandomNumber(): String{
        val random = Random.Default
        var randomNumber = ""
        for (i in 0..5) {
            randomNumber += random.nextInt(0..9).toString()
        }
        return randomNumber
    }

    private suspend fun checkValidCertificationNumber(certificationVerifyRequestDto: CertificationVerifyRequestDto) {
        if (getValues(certificationVerifyRequestDto.phoneNumber) != certificationVerifyRequestDto.certificationNumber)
            throw FailedAuthenticationException
    }

    private suspend fun setValues(phoneNumber: String, randomNumber: String) {
        val values = redisConfig.redisTemplate().opsForValue()
        values.set(phoneNumber, randomNumber, Duration.ofMinutes(5))
    }

    private suspend fun getValues(phoneNumber: String): String? {
        val value = redisConfig.redisTemplate().opsForValue()
        return value.get(phoneNumber)
    }

    private suspend fun deleteValues(phoneNumber: String) {
        redisConfig.redisTemplate().opsForValue().operations.delete(phoneNumber)
    }
}