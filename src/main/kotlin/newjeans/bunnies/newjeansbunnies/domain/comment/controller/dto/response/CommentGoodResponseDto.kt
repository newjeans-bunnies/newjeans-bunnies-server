package newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.response

data class CommentGoodResponseDto(
    val commentId: String,
    val commentGoodState: Boolean,
    val commentCount: Long
)
