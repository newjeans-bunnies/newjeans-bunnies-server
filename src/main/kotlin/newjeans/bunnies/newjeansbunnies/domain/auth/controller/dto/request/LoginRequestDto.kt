package newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.request

import jakarta.validation.constraints.NotBlank

data class LoginRequestDto(
    @field:NotBlank(message = "아이디는 필수 입력 값입니다.")
    val id: String,
    @field:NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    val password: String,
)
