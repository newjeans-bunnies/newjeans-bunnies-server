package newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.response

data class CommentResponseDto(
    val nickname: String,
    val userImageUrl: String?,
    val postId: String,
    val body: String,
    val goodCounts: Long,
    val createDate: String,
    val goodState: Boolean?
)
