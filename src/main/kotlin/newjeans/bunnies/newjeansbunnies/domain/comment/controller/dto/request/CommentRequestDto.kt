package newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.request

data class CommentRequestDto(
    val body: String,
    val userId: String,
    val postId: String
)