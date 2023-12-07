package newjeans.bunnies.newjeansbunnies.domain.comment.error


import newjeans.bunnies.newjeansbunnies.global.error.custom.CustomErrorProperty


enum class CommentErrorCode(
    private val status: Int,
    private val message: String
) : CustomErrorProperty {
    USER_NOT_FOUND(400, "Not Found User"),
    POST_NOT_FOUND(400, "Not Found Post")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}