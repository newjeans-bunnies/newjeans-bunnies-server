package newjeans.bunnies.newjeansbunnies.domain.auth.service

import newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.request.CertificationVerifyRequestDto
import newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.response.CertificationVerifyResponseDto
import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.ExistPhoneNumberException
import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.FailedAuthenticationException
import newjeans.bunnies.newjeansbunnies.domain.user.repository.UserRepository
import newjeans.bunnies.newjeansbunnies.global.config.RedisConfig
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

    fun verify(certificationVerifyRequestDto: CertificationVerifyRequestDto): CertificationVerifyResponseDto {
        if (getValues(certificationVerifyRequestDto.phoneNumber) != certificationVerifyRequestDto.certificationNumber)
            throw FailedAuthenticationException
        else
            deleteValues(certificationVerifyRequestDto.phoneNumber)
        return CertificationVerifyResponseDto(200, "success")
    }

    fun certification(phoneNumber: String): CertificationVerifyResponseDto {
        if(userRepository.findByPhoneNumber(phoneNumber).isPresent)
            throw ExistPhoneNumberException

        val random = Random.Default
        var randomNumber = ""
        for (i in 0..5) {
            randomNumber += random.nextInt(0..9).toString()
        }
        setValues(phoneNumber, randomNumber)
        log.info("[인증번호: $randomNumber] NewJeans-Bunnies 인증번호입니다.")
//        smsUtil.sendOne(phoneNumber, "[인증번호: $randomNumber] NewJeans-Bunnies 인증번호입니다.")
        return CertificationVerifyResponseDto(200, "success")
    }

    private fun setValues(phoneNumber: String, randomNumber: String) {
        val values = redisConfig.redisTemplate().opsForValue()
        values.set(phoneNumber, randomNumber, Duration.ofMinutes(5))
    }

    private fun getValues(phoneNumber: String): String? {
        val value = redisConfig.redisTemplate().opsForValue()
        return value.get(phoneNumber)
    }

    private fun deleteValues(phoneNumber: String) {
        redisConfig.redisTemplate().opsForValue().operations.delete(phoneNumber)
    }
}