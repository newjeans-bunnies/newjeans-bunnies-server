package newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.response


data class ChildrenCommentBasicGetResponseDto(
    val uuid: String,
    val id: String,
    val body: String,
    val good: Long,
    val parentsCommentId: String,
    val createDate: String
)