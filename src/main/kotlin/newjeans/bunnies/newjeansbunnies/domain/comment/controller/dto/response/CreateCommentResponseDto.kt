package newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.response

data class CreateCommentResponseDto(
    val userId: String,
    val postId: String,
    val body: String,
    val createDate: String,
)
