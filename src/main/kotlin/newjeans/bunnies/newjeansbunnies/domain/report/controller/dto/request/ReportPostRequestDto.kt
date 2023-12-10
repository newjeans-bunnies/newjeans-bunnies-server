package newjeans.bunnies.newjeansbunnies.domain.report.controller.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class ReportPostRequestDto(
    @field:NotBlank(message = "아이디는 필수 입력 값입니다.")
    val id: String,
    @field:NotBlank(message = "전화번호는 필수 입력 값입니다.")
    @field:Pattern(regexp = "^(010)-([0-9]{4})-([0-9]{4})$", message = "전화번호를 적어주세요 ex) 010-1234-5678")
    val phoneNumber: String,
    @field:NotBlank(message = "게시글아이디는 필수 입력 값입니다.")
    val postId: String
)
