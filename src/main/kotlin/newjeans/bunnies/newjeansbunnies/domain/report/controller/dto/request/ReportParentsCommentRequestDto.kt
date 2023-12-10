package newjeans.bunnies.newjeansbunnies.domain.report.controller.dto.request

import jakarta.validation.constraints.NotBlank

data class ReportParentsCommentRequestDto(
    @field:NotBlank(message = "아이디는 필수 입력 값입니다.")
    val userId: String,
    @field:NotBlank(message = "아이디는 필수 입력 값입니다.")
    val phoneNumber: String,
    @field:NotBlank(message = "댓글아이디는 필수 입력 값입니다.")
    val parentsCommentId: String,
)
