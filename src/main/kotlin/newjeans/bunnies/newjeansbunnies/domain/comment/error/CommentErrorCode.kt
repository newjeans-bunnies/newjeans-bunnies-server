package newjeans.bunnies.newjeansbunnies.domain.comment.error


import newjeans.bunnies.newjeansbunnies.global.error.custom.CustomErrorProperty


enum class CommentErrorCode(
    private val status: Int,
    private val message: String
) : CustomErrorProperty {
    NOT_FOUND_COMMENT(204,"댓글이 존재하지 않습니다"),
    NO_EDIT_PERMISSION(401, "수정 권한 없음")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}