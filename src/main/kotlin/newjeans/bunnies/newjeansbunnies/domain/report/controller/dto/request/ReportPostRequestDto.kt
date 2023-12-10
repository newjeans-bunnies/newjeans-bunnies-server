package newjeans.bunnies.newjeansbunnies.domain.report.controller.dto.request

import jakarta.validation.constraints.NotBlank

data class ReportPostRequestDto(
    @field:NotBlank(message = "아이디는 필수 입력 값입니다.")
    val id: String,
    @field:NotBlank(message = "전화번호는 필수 입력 값입니다.")
    val phoneNumber: String,
    @field:NotBlank(message = "게시글아이디는 필수 입력 값입니다.")
    val postId: String
)
