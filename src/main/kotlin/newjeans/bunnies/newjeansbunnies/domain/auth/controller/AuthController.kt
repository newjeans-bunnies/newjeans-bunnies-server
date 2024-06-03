package newjeans.bunnies.newjeansbunnies.domain.auth.controller


import jakarta.validation.Valid
import jakarta.validation.constraints.Pattern
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.TokenDto
import newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.request.CertificationVerifyRequestDto
import newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.request.LoginRequestDto
import newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.request.SignupRequestDto
import newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.response.SignupResponseDto
import newjeans.bunnies.newjeansbunnies.domain.auth.service.AuthService
import newjeans.bunnies.newjeansbunnies.domain.auth.service.PhoneNumberCertificationService
import newjeans.bunnies.newjeansbunnies.domain.auth.service.ReissueTokenService
import newjeans.bunnies.newjeansbunnies.global.error.exception.RefreshTokenNotForundException
import newjeans.bunnies.newjeansbunnies.global.response.StatusResponseDto
import newjeans.bunnies.newjeansbunnies.global.security.principle.CustomUserDetails
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*


@RestController
@Configuration
@RequestMapping("/api/auth")
@Validated
class AuthController(
    private val reissueTokenService: ReissueTokenService,
    private val phoneNumberCertificationService: PhoneNumberCertificationService,
    private val authService: AuthService
) {

    @PostMapping("/login")
    fun login(@RequestBody @Valid loginRequestDto: LoginRequestDto): TokenDto {
        return authService.login(loginRequestDto)
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    fun signup(@RequestBody @Valid signupRequestDto: SignupRequestDto): SignupResponseDto {
        runBlocking { delay(5000) }
        return authService.signup(signupRequestDto)
    }

    @PatchMapping("/refresh")
    fun reissueToken(
        @RequestHeader("refresh-token") refreshToken: String,
        @RequestHeader("access-token") accessToken: String,
    ): TokenDto {
        if (refreshToken.isBlank()) throw RefreshTokenNotForundException
        return reissueTokenService.execute(refreshToken, accessToken)
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/phonenumber/verify")
    fun verify(@RequestBody @Valid certificationVerifyRequestDto: CertificationVerifyRequestDto): StatusResponseDto {
        return phoneNumberCertificationService.verify(certificationVerifyRequestDto)
    }

    @PostMapping("/phonenumber")
    fun certification(
        @RequestParam("phonenumber") @Pattern(
            regexp = "^(010)([0-9]{4})([0-9]{4})$", message = "전화번호를 적어주세요 ex) 010-1234-5678"
        ) phoneNumber: String
    ): StatusResponseDto {
        return phoneNumberCertificationService.certification(phoneNumber)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/delete")
    fun deleteUser(
        @RequestParam("user-id") userId: String,
        @AuthenticationPrincipal auth: CustomUserDetails?
    ): StatusResponseDto {
        return authService.userDelete(userId, auth?.username)
    }

}