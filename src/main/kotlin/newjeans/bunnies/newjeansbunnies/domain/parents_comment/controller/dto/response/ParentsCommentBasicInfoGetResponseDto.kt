package newjeans.bunnies.newjeansbunnies.domain.parents_comment.controller.dto.response



data class ParentsCommentBasicInfoGetResponseDto(
    val uuid: String,
    val id: String,
    val body: String,
    val postId: String,
    val createDate: String,
    val good: Long
)
