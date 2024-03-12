package newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.request


import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern


data class SignupRequestDto(
    @field:NotBlank(message = "아이디는 필수 입력 값입니다.")
    @field:Pattern(regexp = "^[a-zA-Z0-9]{4,12}$", message = "아이디는 4~12자 영문 대 소문자, 숫자만 사용하세요.")
    val userId: String,
    @field:NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @field:Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%^&*()])(?=\\S+\$).{8,16}\$", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    val password: String,
    @field:NotBlank(message = "전화번호는 필수 입력 값입니다.")
    @field:Pattern(regexp = "^(010)([0-9]{4})([0-9]{4})$", message = "전화번호를 적어주세요 ex) 010-1234-5678")
    val phoneNumber: String,
    @field:NotBlank(message = "나라는 필수 입력 값입니다.")
    val country: String,
    @field:NotBlank(message = "생년월일은 필수 입력 값입니다.")
    @field:Pattern(regexp = "^([0-9]{8})$", message = "생년월일은 YYYY-MM-DD 형식을 맞춰주세요")
    val birth: String
)
