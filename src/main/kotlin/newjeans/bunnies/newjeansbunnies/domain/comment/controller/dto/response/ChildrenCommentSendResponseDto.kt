package newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.response


data class ChildrenCommentSendResponseDto(
    val uuid: String,
    val id: String,
    val body: String,
    val parentsCommentId: String,
    val createDate: String
)
