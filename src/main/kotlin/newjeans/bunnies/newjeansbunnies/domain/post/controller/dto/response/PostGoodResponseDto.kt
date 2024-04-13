package newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response


data class PostGoodResponseDto(
    val postId: String,
    val goodCounts: Long,
    val goodStatus: Boolean
)
