package newjeans.bunnies.newjeansbunnies.domain.user.controller.dto.request


import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern


data class UserUpdateRequestDto(
    @field:NotBlank(message = "아이디는 필수 입력 값입니다.")
    val userId: String,
    @field:NotBlank(message = "전화번호는 필수 입력 값입니다.")
    @field:Pattern(regexp = "^(010)-([0-9]{4})-([0-9]{4})$", message = "전화번호를 적어주세요 ex) 010-1234-5678")
    val phoneNumber: String,
    val imageName: String?,
    @field:NotBlank(message = "나라는 필수 입력 값입니다.")
    val country: String
)
