package newjeans.bunnies.newjeansbunnies.domain.auth.service

import newjeans.bunnies.newjeansbunnies.domain.auth.CertificationNumberEntity
import newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.request.CertificationVerifyRequestDto
import newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.response.CertificationVerifyResponseDto
import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.FailedAuthenticationException
import newjeans.bunnies.newjeansbunnies.domain.auth.repository.CertificationNumberRepository
import newjeans.bunnies.newjeansbunnies.global.utils.SmsUtil
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import kotlin.random.Random
import kotlin.random.nextInt

@Service
@Configuration
class PhoneNumberCertificationService(
    private val smsUtil: SmsUtil,
    private val phoneNumberNumberRepository: CertificationNumberRepository
) {
    fun verify(certificationVerifyRequestDto: CertificationVerifyRequestDto): CertificationVerifyResponseDto {
        phoneNumberNumberRepository.findByPhoneNumberAndCertificationNumber(
            certificationVerifyRequestDto.phoneNumber,
            certificationVerifyRequestDto.certificationNumber
        ).orElseThrow {
            throw FailedAuthenticationException
        }
        return CertificationVerifyResponseDto(200, "success")
    }

    fun certification(phoneNumber: String): CertificationVerifyResponseDto {
        val random = Random.Default
        var randomNumber = ""
        for (i in 0..3) {
            randomNumber += random.nextInt(0..9).toString()
        }
        val certificationNumberEntity = CertificationNumberEntity(phoneNumber, randomNumber)
        phoneNumberNumberRepository.save(certificationNumberEntity)
        smsUtil.sendOne(phoneNumber, randomNumber)
        return CertificationVerifyResponseDto(200, "success")
    }
}