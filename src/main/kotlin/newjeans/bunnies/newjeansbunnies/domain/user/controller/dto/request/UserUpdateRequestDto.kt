package newjeans.bunnies.newjeansbunnies.domain.user.controller.dto.request


import jakarta.validation.constraints.NotBlank


data class UserUpdateRequestDto(
    @field:NotBlank(message = "아이디는 필수 입력 값입니다.")
    val userId: String,
    @field:NotBlank(message = "나라는 필수 입력 값입니다.")
    val country: String,
    @field:NotBlank(message = "언어는 필수 입력 값입니다.")
    val language: String
)
