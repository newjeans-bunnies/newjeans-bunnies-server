package newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class CertificationVerifyRequestDto(
    @field:NotBlank(message = "전화번호는 필수 입력 값입니다.")
    @field:Pattern(regexp = "^(010)([0-9]{4})([0-9]{4})$", message = "전화번호를 적어주세요 ex) 01012345678")
    val phoneNumber: String,
    @field:NotBlank(message = "인증번호는 필수 입력 값입니다.")
    @field:Pattern(regexp = "^\\d{6}\$", message = "인증번호는 숫자 6글자만 가능합니다")
    val certificationNumber: String
)
