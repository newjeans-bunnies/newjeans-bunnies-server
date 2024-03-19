package newjeans.bunnies.newjeansbunnies.domain.user.controller.dto.request


import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern


data class UserUpdateRequestDto(
    @field:NotBlank(message = "아이디는 필수 입력 값입니다.")
    val userId: String,
    @field:NotBlank(message = "나라는 필수 입력 값입니다.")
    val country: String,
    @field:NotBlank(message = "언어는 필수 입력 값입니다.")
    val language: String,
    @field:NotBlank(message = "생년월일은 필수 입력 값입니다.")
    @field:Pattern(regexp = "^([0-9]{8})$", message = "생년월일은 YYYY-MM-DD 형식을 맞춰주세요")
    val birth: String
)
