package newjeans.bunnies.newjeansbunnies.domain.auth.controller

import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.request.LoginRequestDto
import newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.request.SignupRequestDto
import newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.response.StatusResponseDto
import newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.response.TokenResponseDto
import newjeans.bunnies.newjeansbunnies.domain.auth.service.LoginService
import newjeans.bunnies.newjeansbunnies.domain.auth.service.ReissueTokenService
import newjeans.bunnies.newjeansbunnies.domain.auth.service.SignupService
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseCookie
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
    fun signup(@RequestBody @Valid signupRequestDto: SignupRequestDto): StatusResponseDto {
        return signupService.execute(signupRequestDto)
    }

    @PostMapping("/login")
    fun login(@RequestBody @Valid loginRequestDto: LoginRequestDto, response: HttpServletResponse): TokenResponseDto {
        val tokenData = loginService.execute(loginRequestDto)
        val cookie = ResponseCookie.from("refresh-token", tokenData.refreshToken)
            .maxAge((3 * 24 * 60 * 60).toLong()) //cookie 유효 기간
            .path("/").sameSite("None").httpOnly(true).build()
        response.setHeader("Set-Cookie", cookie.toString())

        return TokenResponseDto(
            accessToken = tokenData.accessToken, authority = tokenData.authority, expiredAt = tokenData.expiredAt
        )
    }

    @PatchMapping("/refresh")
    fun reissueToken(
        @RequestHeader("refresh-token") refreshToken: String, response: HttpServletResponse
    ): TokenResponseDto {
        val tokenData = reissueTokenService.execute(refreshToken)
        val cookie = ResponseCookie.from("refresh-token", tokenData.refreshToken)
            .maxAge((3 * 24 * 60 * 60).toLong()) //cookie 유효 기간
            .path("/").sameSite("None").httpOnly(true).build()
        response.setHeader("Set-Cookie", cookie.toString())

        return TokenResponseDto(
            accessToken = tokenData.accessToken, authority = tokenData.authority, expiredAt = tokenData.expiredAt
        )
    }

}