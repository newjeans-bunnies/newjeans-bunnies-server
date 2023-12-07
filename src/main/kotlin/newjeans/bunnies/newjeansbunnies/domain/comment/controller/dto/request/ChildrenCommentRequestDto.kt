package newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.request


import jakarta.validation.constraints.NotBlank


data class ChildrenCommentRequestDto(
    @field:NotBlank(message = "아이디는 필수 입력 값입니다.")
    val id: String,
    @field:NotBlank(message = "댓글은 필수 입력 값입니다.")
    val body: String,
    @field:NotBlank(message = "댓글ID는 필수 입력 값입니다.")
    val parentId: String
)
