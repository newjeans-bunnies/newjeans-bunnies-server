package newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.request


import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size


data class ParentsCommentRequestDto(
    @field:NotBlank(message = "아이디는 필수 입력 값입니다.")
    val id: String,
    @field:NotBlank(message = "댓글은 필수 입력 값입니다.")
    val body: String,
    @field:NotBlank(message = "게시글ID는 필수 입력 값입니다.")
    @field:Size(min = 36, max = 36, message = "게시글ID를 적어주세요")
    val postId: String
)
