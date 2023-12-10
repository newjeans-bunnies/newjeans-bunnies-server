package newjeans.bunnies.newjeansbunnies.domain.auth.controller


import jakarta.validation.Valid

import newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.TokenDto
import newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.request.LoginRequestDto
import newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.request.SignupRequestDto
import newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.response.SignupResponseDto
import newjeans.bunnies.newjeansbunnies.domain.auth.service.LoginService
import newjeans.bunnies.newjeansbunnies.domain.auth.service.ReissueTokenService
import newjeans.bunnies.newjeansbunnies.domain.auth.service.SignupService
import newjeans.bunnies.newjeansbunnies.global.error.exception.RefreshTokenNotForundException

import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*


@RestController
@Configuration
@RequestMapping("/api/auth")
class AuthController(
    private val loginService: LoginService,
    private val signupService: SignupService,
    private val reissueTokenService: ReissueTokenService
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
        @RequestHeader("refresh-token") refreshToken: String
    ): TokenDto {
        if(refreshToken.isBlank())
            throw RefreshTokenNotForundException
        return reissueTokenService.execute(refreshToken)
    }

}