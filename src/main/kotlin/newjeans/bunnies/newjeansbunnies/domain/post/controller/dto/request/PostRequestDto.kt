package newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.request

import jakarta.validation.constraints.NotBlank

data class PostRequestDto(
    @field:NotBlank(message = "아이디는 필수 입력 값입니다.")
    val id: String,
    @field:NotBlank(message = "게시글 제목 필수 입력 값입니다.")
    val title: String,
    @field:NotBlank(message = "게시글 내용은 필수 입력 값입니다.")
    val body: String
)