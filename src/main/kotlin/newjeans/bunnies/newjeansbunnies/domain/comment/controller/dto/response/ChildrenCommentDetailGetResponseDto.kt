package newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.response


data class ChildrenCommentDetailGetResponseDto(
    val uuid: String,
    val id: String,
    val body: String,
    val good: Long,
    val goodStatus: Boolean,
    val parentsCommentId: String,
    val createDate: String
)
