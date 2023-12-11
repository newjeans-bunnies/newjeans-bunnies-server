package newjeans.bunnies.newjeansbunnies.domain.parents_comment.error


import newjeans.bunnies.newjeansbunnies.global.error.custom.CustomErrorProperty


enum class ParentsCommentErrorCode(
    private val status: Int,
    private val message: String
) : CustomErrorProperty {
    COMMENT_ID_BLANK(400, "댓글아이디는 필수 입력 값입니다."),
    POST_ID_BLANK(400, "게시글아이디는 필수 입력 값입니다."),
    NOT_EXIST_PARENTS_COMMENT_ID(400, "존재하지 않는 댓글아이디 입니다."),
    NOT_EXIST_CHILDREN_COMMENT_ID(400, "존재하지 않는 대댓글아이디 입니다."),
    NOT_EXIST_POST_ID(400, "존재하지 않는 게시글아이디 입니다."),

    ;

    override fun status(): Int = status
    override fun message(): String = message
}