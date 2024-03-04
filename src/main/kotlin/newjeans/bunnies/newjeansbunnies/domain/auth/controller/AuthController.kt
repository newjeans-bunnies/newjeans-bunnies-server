package newjeans.bunnies.newjeansbunnies.domain.auth.controller


import jakarta.validation.Valid
import jakarta.validation.constraints.Pattern
import newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.TokenDto
import newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.request.CertificationVerifyRequestDto
import newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.request.LoginRequestDto
import newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.request.SignupRequestDto
import newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.response.CertificationVerifyResponseDto
import newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.response.SignupResponseDto
import newjeans.bunnies.newjeansbunnies.domain.auth.service.LoginService
import newjeans.bunnies.newjeansbunnies.domain.auth.service.PhoneNumberCertificationService
import newjeans.bunnies.newjeansbunnies.domain.auth.service.ReissueTokenService
import newjeans.bunnies.newjeansbunnies.domain.auth.service.SignupService
import newjeans.bunnies.newjeansbunnies.global.error.exception.RefreshTokenNotForundException
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*


@RestController
@Configuration
@RequestMapping("/api/auth")
@Validated
class AuthController(
    private val loginService: LoginService,
    private val signupService: SignupService,
    private val reissueTokenService: ReissueTokenService,
    private val phoneNumberCertificationService: PhoneNumberCertificationService
) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    fun signup(@RequestBody @Valid signupRequestDto: SignupRequestDto): SignupResponseDto {
        return signupService.execute(signupRequestDto)
    }

    @PostMapping("/login")
    fun login(@RequestBody @Valid loginRequestDto: LoginRequestDto): TokenDto {
        return loginService.execute(loginRequestDto)
    }

    @PatchMapping("/refresh")
    fun reissueToken(
        @RequestHeader("refresh-token") refreshToken: String,
        @RequestHeader("access-token") accessToken: String,
    ): TokenDto {
        if (refreshToken.isBlank())
            throw RefreshTokenNotForundException
        return reissueTokenService.execute(refreshToken, accessToken)
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/phonenumber/verify")
    fun verify(@RequestBody @Valid certificationVerifyRequestDto: CertificationVerifyRequestDto): CertificationVerifyResponseDto {
        return phoneNumberCertificationService.verify(certificationVerifyRequestDto)
    }

    @PostMapping("/phonenumber")
    fun certification(
        @RequestParam("phonenumber") @Pattern(
            regexp = "^(010)([0-9]{4})([0-9]{4})$",
            message = "전화번호를 적어주세요 ex) 010-1234-5678"
        ) phoneNumber: String
    ): CertificationVerifyResponseDto {
        return phoneNumberCertificationService.certification(phoneNumber)
    }

}