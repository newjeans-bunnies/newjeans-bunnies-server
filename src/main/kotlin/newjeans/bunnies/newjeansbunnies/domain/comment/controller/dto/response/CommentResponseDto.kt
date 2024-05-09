package newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.response

data class CommentResponseDto(
    val userId: String,
    val userImageUrl: String,
    val postId: String,
    val body: String,
    val createDate: String
)
