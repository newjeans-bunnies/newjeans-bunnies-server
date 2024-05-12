package newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.response

data class FixCommentResponseDto(
    val userId: String,
    val postId: String,
    val body: String,
    val state: Boolean
)
