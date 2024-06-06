package newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.request


import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull


data class PostRequestDto(
    @field:NotBlank(message = "게시글 내용은 필수 입력 값입니다.")
    @field:NotNull(message = "게시글 내용은 필수 입력 값입니다.")
    val body: String,
    @field:NotBlank(message = "게시글 아이디는 필수 입력 값입니다.")
    @field:NotNull(message = "게시글 아이디는 필수 입력 값입니다.")
    val postId: String,
    @field:NotBlank(message = "게시글 아이디는 필수 입력 값입니다.")
    @field:NotNull(message = "게시글 아이디는 필수 입력 값입니다.")
    val imageId: List<String>,
)